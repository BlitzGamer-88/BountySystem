package com.blitzoffline.bountysystem.config.holder

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path

object Bounty : SettingsHolder {
    @Path("bounties.max")
    val MAX_AMOUNT = 2
    @Path("bounties.expiry")
    val EXPIRY_TIME = 259200
    @Path("bounties.tax")
    val TAX = 5
}