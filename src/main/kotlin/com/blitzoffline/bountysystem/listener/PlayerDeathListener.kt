package com.blitzoffline.bountysystem.listener

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import com.blitzoffline.bountysystem.util.broadcastMessage
import com.blitzoffline.bountysystem.util.containsIgnoreCase
import com.blitzoffline.bountysystem.util.inRegion
import com.blitzoffline.bountysystem.util.parsePAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(private val plugin: BountySystem) : Listener {
    private val messages = plugin.messages
    private val settings = plugin.settings

    @EventHandler(ignoreCancelled = true)
    fun PlayerDeathEvent.onDeath() {
        val killer = entity.killer ?: return

        if (settings[Settings.WORLDS_USE] && !settings[Settings.WORLDS_LIST].containsIgnoreCase(killer.world.name) && !settings[Settings.WORLDS_LIST].containsIgnoreCase(killer.world.uid.toString())) return
        if (settings[Settings.REGIONS_USE] && !killer.location.inRegion(settings)) return

        for (bounty in plugin.bountyHandler.bounties) {
            if (bounty.target != entity.uniqueId) continue
            if (bounty.payer == killer.uniqueId) continue
            if (plugin.bountyHandler.expired(bounty)) continue

            val afterTax = bounty.amount - ((settings[Bounties.TAX] / 100) * bounty.amount)

            plugin.economy.depositPlayer(killer, afterTax.toDouble())

            plugin.bountyHandler.bounties.remove(bounty)

            messages[Messages.BOUNTY_RECEIVED_BROADCAST]
                .replace("%amount%", afterTax.toString())
                .replace("%target%", entity.name)
                .parsePAPI(killer)
                .broadcastMessage()

            messages[Messages.BOUNTY_RECEIVED]
                .replace("%amount%", afterTax.toString())
                .replace("%target%", entity.name )
                .sendMessage(killer)
        }
    }

}