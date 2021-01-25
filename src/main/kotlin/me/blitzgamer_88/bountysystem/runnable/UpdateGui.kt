package me.blitzgamer_88.bountysystem.runnable

import me.blitzgamer_88.bountysystem.util.bountyGui
import org.bukkit.scheduler.BukkitRunnable

class UpdateGui : BukkitRunnable() {
    override fun run() {
        bountyGui.update()
    }
}