package me.blitzgamer_88.bountysystem.runnable

import me.blitzgamer_88.bountysystem.BountySystem
import org.bukkit.scheduler.BukkitRunnable

class SaveCache(private val plugin: BountySystem) : BukkitRunnable() {
    override fun run() = plugin.database.save()
}