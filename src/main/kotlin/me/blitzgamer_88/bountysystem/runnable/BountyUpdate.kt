package me.blitzgamer_88.bountysystem.runnable

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.util.gui.updateGui
import org.bukkit.scheduler.BukkitRunnable

class BountyUpdate(private val plugin: BountySystem) : BukkitRunnable() {

    override fun run() {
        updateGui(plugin)
    }
}