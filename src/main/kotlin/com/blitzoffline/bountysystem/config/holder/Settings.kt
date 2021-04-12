package com.blitzoffline.bountysystem.config.holder

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path

object Settings : SettingsHolder {
    @Path("regions.use")
    val REGIONS_USE = false
    @Path("regions.list")
    val REGIONS_LIST = listOf("warzone", "warzone2")

    @Path("worlds.use")
    val WORLDS_USE = false
    @Path("worlds.list")
    val WORLDS_LIST = listOf("world", "world_the_end")

    @Path("intervals.expiry")
    val INTERVAL_EXPIRY = 300
    @Path("intervals.cache")
    val INTERVAL_CACHE = 300
}