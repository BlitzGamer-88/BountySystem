package com.blitzoffline.bountysystem.listener

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.config.settings
import com.blitzoffline.bountysystem.util.broadcast
import com.blitzoffline.bountysystem.util.containsIgnoreCase
import com.blitzoffline.bountysystem.util.inRegion
import com.blitzoffline.bountysystem.util.msg
import com.blitzoffline.bountysystem.util.parsePAPI
import java.text.DecimalFormat
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener : Listener {

    @EventHandler(ignoreCancelled = true)
    fun PlayerDeathEvent.onDeath() {
        val killer = entity.killer ?: return

        if (settings[Settings.WORLDS_USE] && !settings[Settings.WORLDS_LIST].containsIgnoreCase(killer.world.name) && !settings[Settings.WORLDS_LIST].containsIgnoreCase(killer.world.uid.toString())) return
        if (settings[Settings.REGIONS_USE] && !killer.location.inRegion()) return

        for (bounty in BOUNTIES_LIST) {
            if (bounty.target != entity.uniqueId) continue
            if (bounty.payer == killer.uniqueId) continue
            if (bounty.expired()) continue

            econ.depositPlayer(killer, bounty.afterTax().toDouble())

            BOUNTIES_LIST.remove(bounty)

            messages[Messages.BOUNTY_RECEIVED_BROADCAST]
                .replace("%amount%", bounty.afterTax().toString())
                .replace("%target%", entity.name)
                .parsePAPI(killer)
                .broadcast()

            messages[Messages.BOUNTY_RECEIVED]
                .replace("%amount%", bounty.afterTax().toString())
                .replace("%target%", entity.name )
                .msg(killer)
        }
    }

}