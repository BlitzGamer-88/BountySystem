package me.blitzgamer_88.bountysystem.listener

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.conf.Config
import me.blitzgamer_88.bountysystem.util.chat.broadcast
import me.blitzgamer_88.bountysystem.util.chat.color
import me.blitzgamer_88.bountysystem.util.chat.msg
import me.blitzgamer_88.bountysystem.util.conf.conf
import me.blitzgamer_88.bountysystem.util.conf.econ
import me.blitzgamer_88.bountysystem.util.conf.maxId
import me.blitzgamer_88.bountysystem.util.conf.minId
import me.blitzgamer_88.bountysystem.util.gui.updateGui
import me.blitzgamer_88.bountysystem.util.wg.locationInWGRegion
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

class PlayerDeathListener(private val plugin: BountySystem) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDeath (event: PlayerDeathEvent) {
        val killed = event.entity
        val killer = killed.killer ?: return

        val killerLocation = killer.location
        val killerWorld = killer.world

        val bounties = plugin.BOUNTIES_LIST
        val ids = bounties.keys

        val bountyReceived = conf().getProperty(Config.bountyReceived)
        val bountyReceivedBroadcast = conf().getProperty(Config.bountyReceivedBroadcast).color()
        val useWorlds = conf().getProperty(Config.useWorlds)
        val useRegions = conf().getProperty(Config.useRegions)
        val enabledWorlds = conf().getProperty(Config.enabledWorlds)
        val bountyTax = conf().getProperty(Config.bountyTax)

        for (id in ids) {

            val newId = id.toIntOrNull() ?: continue
            if (newId < minId || newId > maxId) continue

            val bounty = bounties[id] ?: continue

            val payerUniqueIdString = bounty.payer ?: continue
            val payerUniqueId = UUID.fromString(payerUniqueIdString)

            val targetUniqueIdString = bounty.target ?: continue
            val targetUniqueId = UUID.fromString(targetUniqueIdString)
            val targetOfflinePlayer = Bukkit.getOfflinePlayer(targetUniqueId)
            val targetOnlinePlayer = targetOfflinePlayer.player ?: continue

            if (targetOnlinePlayer != killed) continue
            if (killer.uniqueId == payerUniqueId) return
            if (killed.uniqueId != targetUniqueId) continue
            if (useWorlds && !enabledWorlds.contains(killerWorld.name)) return
            if (useRegions && !locationInWGRegion(killerLocation)) return

            val amount = bounty.amount ?: continue
            val newAmount = amount - ((bountyTax/100)*amount)
            econ?.depositPlayer(killer, newAmount.toDouble())
            plugin.BOUNTIES_LIST.remove(id)
            updateGui(plugin)
            bountyReceived.replace("%amount%", newAmount.toString()).replace("%target%", killed.name ).msg(killer)
            PlaceholderAPI.setPlaceholders(killer, bountyReceivedBroadcast.replace("%amount%", newAmount.toString()).replace("%target%", killed.name)).broadcast()
            return
        }
    }
}