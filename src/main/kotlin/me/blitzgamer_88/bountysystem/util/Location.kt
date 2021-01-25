package me.blitzgamer_88.bountysystem.util

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import org.bukkit.Location

fun Location.isInCorrectWorldGuardRegion() : Boolean {
    val weLocation = BukkitAdapter.adapt(this)
    val container = WorldGuard.getInstance().platform.regionContainer

    container.createQuery().getApplicableRegions(weLocation).forEach {
        if (enabledRegions.contains(it.id)) return true
    }
    return false
}