package com.blitzoffline.bountysystem.runnable

import com.blitzoffline.bountysystem.BountySystem
import org.bukkit.scheduler.BukkitRunnable

class SaveCache(private val plugin: BountySystem) : BukkitRunnable() { override fun run() = plugin.database.save() }