package me.blitzgamer_88.bountysystem.util.gui

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.conf.Config
import me.blitzgamer_88.bountysystem.util.chat.color
import me.blitzgamer_88.bountysystem.util.chat.parsePAPI
import me.blitzgamer_88.bountysystem.util.conf.conf
import me.blitzgamer_88.bountysystem.util.conf.maxId
import me.blitzgamer_88.bountysystem.util.conf.minId
import me.blitzgamer_88.bountysystem.util.time.formatTime
import me.mattstudios.mfgui.gui.guis.GuiItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

fun updateGui(plugin: BountySystem) {
    // Messages
    val itemTitle = conf().getProperty(Config.itemTitle)
    val itemLore = conf().getProperty(Config.itemLore)
    // Others
    val ids = plugin.BOUNTIES_LIST.keys
    val bountyTax = conf().getProperty(Config.bountyTax)
    val bountyExpiryTime = conf().getProperty(Config.bountyExpiryTime)

    // CREATE A MENU WITH ALL BOUNTIES LISTED INSIDE

    val bounties = plugin.BOUNTIES_LIST

    var i = 0
    for (id in ids) {
        if (id.toIntOrNull() == null) continue
        if (id.toInt() < minId || id.toInt() > maxId) continue

        val bounty = bounties[id] ?: continue

        val targetUniqueIdString = bounty.target ?: continue
        val targetUniqueId = UUID.fromString(targetUniqueIdString)
        val targetOfflinePlayer = Bukkit.getOfflinePlayer(targetUniqueId)

        val payerUniqueIdString = bounty.payer ?: continue
        val payerUniqueId = UUID.fromString(payerUniqueIdString)
        val payerOfflinePlayer = Bukkit.getOfflinePlayer(payerUniqueId)
        val payerName = payerOfflinePlayer.name ?: continue

        val amount = bounty.amount ?: continue
        val newAmount = amount - ((bountyTax / 100) * amount)
        val placedTime = bounty.placedTime ?: continue
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
}