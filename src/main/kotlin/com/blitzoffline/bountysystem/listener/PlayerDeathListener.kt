package com.blitzoffline.bountysystem.listener

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.config.settings
import com.blitzoffline.bountysystem.runnable.minId
import com.blitzoffline.bountysystem.util.broadcast
import com.blitzoffline.bountysystem.util.isInCorrectWorldGuardRegion
import com.blitzoffline.bountysystem.util.msg
import com.blitzoffline.bountysystem.util.parsePAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

class PlayerDeathListener : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onPlayerDeath (event: PlayerDeathEvent) {
        val killed = event.entity
        val killer = killed.killer ?: return

        for (bounty in BOUNTIES_LIST.values) {
            if (bounty.id < minId) continue
            if (UUID.fromString(bounty.target) != killed.uniqueId) continue
            if (UUID.fromString(bounty.payer) == killer.uniqueId) continue
            if (settings[Settings.WORLDS_USE] && !settings[Settings.WORLDS_LIST].contains(killer.world.name)) return
            if (settings[Settings.REGIONS_USE] && !killer.location.isInCorrectWorldGuardRegion(settings[Settings.WORLDS_LIST])) return

            val finalAmount = bounty.amount - ((settings[Bounties.TAX] / 100) * bounty.amount)
            econ.depositPlayer(killer, finalAmount.toDouble())
            BOUNTIES_LIST.remove(bounty.id.toString())
            messages[Messages.BOUNTY_RECEIVED]
                .replace("%amount%", finalAmount.toString())
                .replace("%target%", killed.name )
                .msg(killer)
            messages[Messages.BOUNTY_RECEIVED_BROADCAST]
                .replace("%amount%", finalAmount.toString())
                .replace("%target%", killed.name)
                .parsePAPI(killer)
                .broadcast()
        }
    }
}