package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Menu
import com.blitzoffline.bountysystem.config.settings
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.PaginatedGui
import net.kyori.adventure.text.Component
import org.bukkit.Material

fun createGUI(): PaginatedGui {
    val bountyGui = PaginatedGui(6, 45, Component.text(settings[Menu.TITLE]))
    bountyGui.setDefaultClickAction { it.isCancelled = true; }
    bountyGui.placeItems()
    /*
    Not using this because there is no consistent way to clear the existing items from the GUI yet,
    resulting in the items duplicating each 20 Ticks instead of updating the old ones.

    var task: BukkitTask? = null
    bountyGui.setOpenGuiAction { task = plugin.server.scheduler.runTaskTimer(plugin, Runnable { bountyGui.clearItems(); bountyGui.placeItems(); bountyGui.update() }, 20L, 20L) }
    bountyGui.setCloseGuiAction { if (task != null && !task!!.isCancelled) task!!.cancel(); task = null }
     */
    return bountyGui
}

fun PaginatedGui.placeItems() {
    val paginatedGui = this
    paginatedGui.filler.fillBottom(
        ItemBuilder
            .from(Material.valueOf(settings[Menu.FILLER_MATERIAL].uppercase()))
            .name(Component.text(settings[Menu.FILLER_NAME]))
            .lore(settings[Menu.FILLER_LORE].map { Component.text(it) })
            .asGuiItem()
    )
    paginatedGui.setItem(
        47,
        ItemBuilder
            .from(Material.valueOf(settings[Menu.PREVIOUS_PAGE_MATERIAL].uppercase()))
            .name(Component.text(settings[Menu.PREVIOUS_PAGE_NAME]))
            .lore(settings[Menu.PREVIOUS_PAGE_LORE].map { Component.text(it) })
            .asGuiItem { paginatedGui.previous() }
    )
    paginatedGui.setItem(
        51,
        ItemBuilder
            .from(Material.valueOf(settings[Menu.NEXT_PAGE_MATERIAL].uppercase()))
            .name(Component.text(settings[Menu.NEXT_PAGE_NAME]))
            .lore(settings[Menu.NEXT_PAGE_LORE].map { Component.text(it) })
            .asGuiItem { paginatedGui.next() }

    )

    for (bounty in BOUNTIES_LIST) {
        if (bounty.expired()) continue

        val payerName = bounty.payer().name ?: continue
        val finalAmount = bounty.amount - ((settings[Bounties.TAX] / 100) * bounty.amount)
        val currTime = System.currentTimeMillis()
        val expiryTime = (settings[Bounties.EXPIRY_TIME] - (currTime - bounty.placedTime) / 1000).format()

        paginatedGui.addItem(
            ItemBuilder
                .skull()
                .name(
                    Component.text(
                        settings[Menu.BOUNTY_NAME]
                            .replace("%amount%", finalAmount.toString())
                            .replace("%bountyId%", bounty.id.toString())
                            .replace("%expiryTime%", expiryTime)
                            .replace("%payer%", payerName)
                            .parsePAPI(bounty.target())
                    )
                )
                .lore(
                    settings[Menu.BOUNTY_LORE].map {
                        Component.text( it
                            .replace("%amount%", finalAmount.toString())
                            .replace("%bountyId%", bounty.id.toString())
                            .replace("%expiryTime%", expiryTime)
                            .replace("%payer%", payerName)
                            .parsePAPI(bounty.target())
                        )
                    }
                )
                .owner(bounty.target())
                .asGuiItem()
        )
    }
}