package me.blitzgamer_88.bountysystem.runnable

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.util.gui.Bounty
import org.bukkit.scheduler.BukkitRunnable

class GetCache(private val plugin: BountySystem) : BukkitRunnable() {
    override fun run() {
        val savedBounties = plugin.getBounties()
        val keys = savedBounties.getKeys(false)

        val bounties = plugin.BOUNTIES_LIST

        if (bounties.isNotEmpty()) {
            SaveCache(plugin).runTask(plugin)
            bounties.clear()
        }

        for (key in keys) {
            val bounty = savedBounties.get(key) as Bounty? ?: continue
            bounties[key] = bounty
        }
    }
}