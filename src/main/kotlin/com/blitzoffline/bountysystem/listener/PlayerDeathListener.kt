package com.blitzoffline.bountysystem.listener

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.bounty.minId
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.config.settings
import com.blitzoffline.bountysystem.util.broadcast
import com.blitzoffline.bountysystem.util.containsIgnoreCase
import com.blitzoffline.bountysystem.util.isInCorrectWorldGuardRegion
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
        if (settings[Settings.REGIONS_USE] && !killer.location.isInCorrectWorldGuardRegion()) return

        for (bounty in BOUNTIES_LIST.values) {
            if (bounty.id < minId) continue
            if (bounty.target != entity.uniqueId) continue
            if (bounty.payer == killer.uniqueId) continue

            val formatter = DecimalFormat("#.##")

            val finalAmount = bounty.amount - ((settings[Bounties.TAX] / 100.0) * bounty.amount)
            econ.depositPlayer(killer, finalAmount)
            BOUNTIES_LIST.remove(bounty.id.toString())
            messages[Messages.BOUNTY_RECEIVED]
                .replace("%amount%", formatter.format(finalAmount))
                .replace("%target%", entity.name )
                .msg(killer)
            messages[Messages.BOUNTY_RECEIVED_BROADCAST]
                .replace("%amount%", formatter.format(finalAmount))
                .replace("%target%", entity.name)
                .parsePAPI(killer)
                .broadcast()
        }
    }

}