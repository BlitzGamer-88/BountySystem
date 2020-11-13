package me.blitzgamer_88.bountysystem.runnable

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.util.gui.Bounty
import org.bukkit.scheduler.BukkitRunnable

class SaveCache(private val plugin: BountySystem) : BukkitRunnable() {
    override fun run() {
        val savedBounties = plugin.getBounties()
        val bounties = plugin.BOUNTIES_LIST

        if (bounties.isEmpty()) return

        val keys = bounties.keys
        val IDs = savedBounties.getKeys(false)

        for (ID in IDs) {
            if (!keys.contains(ID)) savedBounties.set(ID, null)
        }

        for (key in keys) {
            val bounty = bounties[key] ?: continue
            savedBounties.set(key, bounty)
        }
        plugin.saveBounties()
    }
}