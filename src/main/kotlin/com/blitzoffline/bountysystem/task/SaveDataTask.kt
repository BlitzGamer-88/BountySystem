package com.blitzoffline.bountysystem.task

import com.blitzoffline.bountysystem.BountySystem
import org.bukkit.scheduler.BukkitRunnable

class SaveDataTask(private val plugin: BountySystem) : BukkitRunnable() { override fun run() = plugin.database.save() }