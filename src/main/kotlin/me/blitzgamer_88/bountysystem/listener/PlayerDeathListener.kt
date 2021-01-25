package me.blitzgamer_88.bountysystem.listener

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.util.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

class PlayerDeathListener : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDeath (event: PlayerDeathEvent) {

        val killed = event.entity
        val killer = killed.killer ?: return

        for (bounty in BountySystem.BOUNTIES_LIST.values) {
            if (bounty.id < minId || bounty.id > maxId) continue
            if (UUID.fromString(bounty.target) != killed.uniqueId) continue
            if (UUID.fromString(bounty.payer) == killer.uniqueId) continue
            if (useWorlds && !enabledWorlds.contains(killer.world.name)) return
            if (useRegions && !killer.location.isInCorrectWorldGuardRegion()) return

            val finalAmount = bounty.amount - ((bountyTax / 100) * bounty.amount)
            econ.depositPlayer(killer, finalAmount.toDouble())
            BountySystem.BOUNTIES_LIST.remove(bounty.id.toString())
            bountyReceived
                .replace("%amount%", finalAmount.toString())
                .replace("%target%", killed.name )
                .msg(killer)
            bountyReceivedBroadcast
                .replace("%amount%", finalAmount.toString())
                .replace("%target%", killed.name)
                .parsePAPI(killer)
                .broadcast()
        }
    }
}