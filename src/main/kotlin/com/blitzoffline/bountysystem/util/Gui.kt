package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Menu
import com.blitzoffline.bountysystem.config.settings
import com.blitzoffline.bountysystem.runnable.minId
import me.mattstudios.mfgui.gui.components.ItemBuilder
import me.mattstudios.mfgui.gui.guis.PaginatedGui
import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.*

fun createGUI(): PaginatedGui {
    val bountyGui = PaginatedGui(6, 45, settings[Menu.TITLE].color())
    bountyGui.setDefaultClickAction { it.isCancelled = true; }
    bountyGui.placeItems()
    /*
    Not using this because there is no consistent way to clear the existing items from the GUI yet,
    resulting in the items duplicating each 20 Ticks instead of updating the old ones.

    var task: BukkitTask? = null
    bountyGui.setOpenGuiAction { task = plugin.server.scheduler.runTaskTimer(plugin, Runnable { bountyGui.placeItems(); bountyGui.update() }, 20L, 20L) }
    bountyGui.setCloseGuiAction { if (task != null && !task!!.isCancelled) task!!.cancel(); task = null }
     */
    return bountyGui
}

fun PaginatedGui.placeItems() {
    val paginatedGui = this
    paginatedGui.filler.fillBottom(
        ItemBuilder
            .from(Material.valueOf(settings[Menu.FILLER_MATERIAL].toUpperCase()))
            .setName(settings[Menu.FILLER_NAME].color())
            .setLore(settings[Menu.FILLER_LORE].color())
            .asGuiItem()
    )
    paginatedGui.setItem(
        47,
        ItemBuilder
            .from(Material.valueOf(settings[Menu.PREVIOUS_PAGE_MATERIAL].toUpperCase()))
            .setName(settings[Menu.PREVIOUS_PAGE_NAME].color())
            .setLore(settings[Menu.PREVIOUS_PAGE_LORE].color())
            .asGuiItem { paginatedGui.previous() }
    )
    paginatedGui.setItem(
        51,
        ItemBuilder
            .from(Material.valueOf(settings[Menu.NEXT_PAGE_MATERIAL].toUpperCase()))
            .setName(settings[Menu.NEXT_PAGE_NAME].color())
            .setLore(settings[Menu.NEXT_PAGE_LORE].color())
            .asGuiItem { paginatedGui.next() }
    )

    for (entry in BOUNTIES_LIST) {
        val id = entry.key.toIntOrNull() ?: continue
        if (id < minId) continue

        val bounty = entry.value

        val targetOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.target))
        val payerOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.payer))

        val payerName = payerOfflinePlayer.name ?: continue
        val finalAmount = bounty.amount - ((settings[Bounties.TAX] / 100) * bounty.amount)
        val currentTime = System.currentTimeMillis() / 1000
        val expiryTime = formatTime(settings[Bounties.EXPIRY_TIME] - (currentTime - bounty.placedTime))

        paginatedGui.addItem(
            ItemBuilder
                .from(Material.PLAYER_HEAD)
                .setLore(
                    settings[Menu.BOUNTY_LORE].map { it
                        .replace("%amount%", finalAmount.toString())
                        .replace("%bountyId%", id.toString())
                        .replace("%expiryTime%", expiryTime)
                        .replace("%payer%", payerName)
                        .parsePAPI(targetOfflinePlayer)
                        .color()
                    }
                )
                .setName(
                    settings[Menu.BOUNTY_NAME]
                        .replace("%amount%", finalAmount.toString())
                        .replace("%bountyId%", id.toString())
                        .replace("%expiryTime%", expiryTime)
                        .replace("%payer%", payerName)
                        .parsePAPI(targetOfflinePlayer)
                        .color()
                )
                .setSkullOwner(targetOfflinePlayer)
                .asGuiItem()
        )
    }
}