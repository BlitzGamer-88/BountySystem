package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.runnable.minId
import me.mattstudios.mfgui.gui.components.ItemBuilder
import me.mattstudios.mfgui.gui.guis.PaginatedGui
import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.*

fun createGUI(plugin: BountySystem) : PaginatedGui {
    val bountyGui = PaginatedGui(6, 45, guiTitle)
    bountyGui.setItem(
        6,
        3,
        ItemBuilder
            .from(Material.PAPER)
            .setName(previousPageName)
            .asGuiItem { bountyGui.previous() }
    )
    bountyGui.setItem(
        6,
        7,
        ItemBuilder
            .from(Material.PAPER)
            .setName(nextPagName)
            .asGuiItem { bountyGui.next() }
    )
    bountyGui.filler.fillBottom(
        ItemBuilder
            .from(fillerItemMaterial)
            .setName(fillerItemName)
            .asGuiItem()
    )

    val bounties = plugin.BOUNTIES_LIST

    for (entry in bounties) {
        val id = entry.key.toIntOrNull() ?: continue
        if (id < minId) continue
        val bounty = entry.value

        val targetOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.target))
        val payerOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.payer))

        val payerName = payerOfflinePlayer.name ?: continue
        val finalAmount = bounty.amount - ((bountyTax / 100) * bounty.amount)
        val currentTime = System.currentTimeMillis() / 1000
        val expiryTime = formatTime(bountyExpiryTime - (currentTime - bounty.placedTime))

        val newItemLore: MutableList<String> = mutableListOf()
        for (line in itemLore) {
            newItemLore.add(
                line
                    .replace("%amount%", finalAmount.toString())
                    .replace("%bountyId%", id.toString())
                    .replace("%expiryTime%", expiryTime)
                    .replace("%payer%", payerName)
            )
        }

        bountyGui.addItem(
            ItemBuilder
                .from(Material.PLAYER_HEAD)
                .setLore(newItemLore)
                .setName(
                    itemName
                    .replace("%amount%", finalAmount.toString())
                    .replace("%bountyId%", id.toString())
                    .replace("%expiryTime%", expiryTime)
                    .replace("%payer%", payerName)
                    .parsePAPI(targetOfflinePlayer)
                )
                .setSkullOwner(targetOfflinePlayer)
                .asGuiItem()
        )
    }
    bountyGui.setDefaultClickAction { it.isCancelled = true }

    return bountyGui
}