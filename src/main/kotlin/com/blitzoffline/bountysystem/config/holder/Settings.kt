package com.blitzoffline.bountysystem.config.holder

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Settings : SettingsHolder {

    @Path("regions.use")
    val REGIONS_USE = Property.create(false)
    @Path("regions.list")
    val REGIONS_LIST = Property.create(listOf("warzone", "warzone2"))

    @Path("worlds.use")
    val WORLDS_USE = Property.create(false)
    @Path("worlds.list")
    val WORLDS_LIST = Property.create(listOf("world", "world_the_end"))

    @Path("intervals.expiry")
    val INTERVAL_EXPIRY = Property.create(300)
    @Path("intervals.cache")
    val INTERVAL_CACHE = Property.create(300)

    @Path("debug")
    val DEBUG = Property.create(false)

}