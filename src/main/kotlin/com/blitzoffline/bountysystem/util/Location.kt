package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.config.holder.Settings
import com.blitzoffline.bountysystem.config.settings
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import org.bukkit.Location

fun Location.isInCorrectWorldGuardRegion() : Boolean {
    val weLocation = BukkitAdapter.adapt(this)
    val container = WorldGuard.getInstance().platform.regionContainer

    return container.createQuery().getApplicableRegions(weLocation).map { it.id }.containsAnyIgnoreCase(settings[Settings.REGIONS_LIST])
}