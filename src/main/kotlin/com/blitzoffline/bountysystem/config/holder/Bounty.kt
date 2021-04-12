package com.blitzoffline.bountysystem.config.holder

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Bounty : SettingsHolder {
    @Path("bounties.max")
    val MAX_AMOUNT = Property.create(2)
    @Path("bounties.expiry")
    val EXPIRY_TIME = Property.create(259200)
    @Path("bounties.tax")
    val TAX = Property.create(5)
}