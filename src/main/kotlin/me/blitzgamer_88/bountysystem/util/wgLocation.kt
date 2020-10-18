package me.blitzgamer_88.bountysystem.util

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import me.blitzgamer_88.bountysystem.conf.Config
import org.bukkit.Location

fun locationInWGRegion(loc: Location) : Boolean {

    val weLocation: com.sk89q.worldedit.util.Location = BukkitAdapter.adapt(loc)
    val container = WorldGuard.getInstance().platform.regionContainer
    val query = container.createQuery()
    val regionSet = query.getApplicableRegions(weLocation)

    val enabledRegions = conf().getProperty(Config.enabledRegions)

    for (region in regionSet) {
        if (enabledRegions.contains(region.id)) {
            return true
        }
    }
    return false

}