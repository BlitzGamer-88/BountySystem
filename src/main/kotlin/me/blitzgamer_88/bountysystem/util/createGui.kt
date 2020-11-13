package me.blitzgamer_88.bountysystem.util

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.conf.Config
import me.mattstudios.mfgui.gui.components.ItemBuilder
import me.mattstudios.mfgui.gui.guis.GuiItem
import me.mattstudios.mfgui.gui.guis.PaginatedGui
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

var bountyGui: PaginatedGui? = null

fun loadDefaultGui(plugin: BountySystem) {

    val guiTitle = conf().getProperty(Config.guiTitle).color()
    bountyGui = PaginatedGui(6, 45, guiTitle)

    // Messages
    val itemTitle = conf().getProperty(Config.itemTitle).color()
    val itemLore = conf().getProperty(Config.itemLore)
    // Others
    val ids = plugin.getBounties().getKeys(false)
    val bountyTax = conf().getProperty(Config.bountyTax)
    val bountyExpiryTime = conf().getProperty(Config.bountyExpiryTime)

    // CREATE A MENU WITH ALL BOUNTIES LISTED INSIDE

    val bounties = plugin.getBounties()

    bountyGui?.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName("&6Previous".color()).asGuiItem { bountyGui?.previous() })
    bountyGui?.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName("&6Next".color()).asGuiItem { bountyGui?.next() })

    val fillerGlass = ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1)
    val fillerMeta = fillerGlass.itemMeta
    if (fillerMeta != null) {
        fillerMeta.setDisplayName(" ")
        fillerGlass.itemMeta = fillerMeta
    }
    val fillerItem = GuiItem(fillerGlass)
    bountyGui?.filler?.fillBottom(fillerItem)

    var i = 0
    for (id in ids) {
        if (id.toIntOrNull() == null) continue
        if (id.toInt() < minId || id.toInt() > maxId) continue

        val targetUniqueIdString = bounties.getString("$id.target") ?: continue
        val targetUniqueId = UUID.fromString(targetUniqueIdString)
        val targetOfflinePlayer = Bukkit.getOfflinePlayer(targetUniqueId)

        val payerUniqueIdString = bounties.getString("$id.placer") ?: continue
        val payerUniqueId = UUID.fromString(payerUniqueIdString)
        val payerOfflinePlayer = Bukkit.getOfflinePlayer(payerUniqueId)
        val payerName = payerOfflinePlayer.name ?: continue

        val amount = bounties.getString("$id.amount") ?: continue
        val newAmount = amount.toInt() - ((bountyTax / 100) * amount.toInt())
        val placedTime = bounties.getLong("$id.placedTime")
        val currentTime = System.currentTimeMillis() / 1000
        val expiryTime = formatTime(bountyExpiryTime - (currentTime - placedTime))

        val newItemLore: MutableList<String> = mutableListOf()

        for (lore in itemLore) {
            newItemLore.add(
                lore.color().replace("%amount%", newAmount.toString()).replace("%payer%", payerName)
                    .replace("%bountyId%", id).replace("%expiryTime%", expiryTime)
            )
        }

        val head = ItemStack(Material.PLAYER_HEAD, 1)

        val meta = head.itemMeta as SkullMeta
        meta.owningPlayer = targetOfflinePlayer
        meta.setDisplayName(itemTitle.replace("%amount%", newAmount.toString()).replace("%payer%", payerName).replace("%bountyId%", id).replace("%expiryTime%", expiryTime).parsePAPI(targetOfflinePlayer))
        meta.lore = newItemLore
        head.itemMeta = meta

        val guiItem = GuiItem(head)
        bountyGui?.setItem(i, guiItem)

        i++
    }

    bountyGui?.setDefaultClickAction { it.isCancelled = true }
}