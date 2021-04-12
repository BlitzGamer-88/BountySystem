package com.blitzoffline.bountysystem.config.holder

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path


object Messages : SettingsHolder {
    @Path("PLAYER-NOT-FOUND")
    val PLAYER_NOT_FOUND = "&7Could not find player. Make sure the player is online and you've typed the correct name."

    @Path("NOT-YOUR-BOUNTY")
    val NOT_YOUR_BOUNTY = "&7Bounty with id &c%bountyId%&7 was not placed by you."

    @Path("BOUNTY-NOT-FOUND")
    val BOUNTY_NOT_FOUND = "&7Could not find bounty with id &c%bountyId%&7."

    @Path("CONFIG-RELOADED")
    val CONFIG_RELOADED = "&aConfig Reloaded."

    @Path("WRONG-USAGE")
    val WRONG_USAGE = "&cWrong Usage."

    @Path("NO-PERMISSION")
    val NO_PERMISSION = "&cYou don't have permission for this."

    @Path("NOT-ENOUGH-MONEY")
    val NOT_ENOUGH_MONEY = "&7You don't have enough money."

    @Path("TARGET-WHITELISTED")
    val TARGET_WHITELISTED = "&7You can't place a bounty on that player."

    @Path("NO-BOUNTIES-FOUND")
    val NO_BOUNTIES_FOUND = "&7There are no bounties at the moment."

    @Path("BOUNTY-ON-YOURSELF")
    val BOUNTY_ON_YOURSELF = "&7You can't place a bounty on yourself."

    @Path("BOUNTY-PLACED-SELF")
    val BOUNTY_PLACED_SELF = "&7You've placed a bounty on &a%target%&7 of &e%amount%\$&7. Bounty ID: &a%bountyId%."

    @Path("BOUNTY-PLACED-EVERYONE")
    val BOUNTY_PLACED_EVERYONE = "&a%player_name%&7 has placed a &e%amount%$&7 bounty on &a%target%&7's head!"

    @Path("BOUNTY-CANCELED")
    val BOUNTY_CANCELED = "&7You have canceled the bounty with the id &c%bountyId%&7."

    @Path("BOUNTY-CANCELED-BY-ADMIN")
    val BOUNTY_CANCELED_ADMIN = "&7An admin has canceled your bounty with the id &c%bountyId%&7."

    @Path("PLAYER-WHITELISTED")
    val PLAYER_WHITELISTED = "&7You have whitelisted &c%player_name%&7."

    @Path("BOUNTY-RECEIVED")
    val BOUNTY_RECEIVED = "&7You have killed %target% and collected a &e%amount%$&7 bounty."

    @Path("MAX-BOUNTIES")
    val MAX_BOUNTIES = "&7You have reached the maximum amounts of bounties you can place at a time."

    @Path("BOUNTY-EXPIRED")
    val BOUNTY_EXPIRED = "&7Your bounty has expired and no one claimed it."

    @Path("BOUNTY-RECEIVED-BROADCAST")
    val BOUNTY_RECEIVED_BROADCAST = "&c%player_name%&7 has killed &e%target%&7 and received &e%amount%$&7."

    @Path("AMOUNT-UPDATED")
    val AMOUNT_UPDATED = "&7Added another &e%newAmount%$ &7to the old amount of &e%oldAmount%$&7."

    @Path("TIME-FORMAT.days")
    val TIME_DAYS = "d "
    @Path("TIME-FORMAT.hours")
    val TIME_HOURS = "h "
    @Path("TIME-FORMAT.minutes")
    val TIME_MINUTES = "m "
    @Path("TIME-FORMAT.seconds")
    val TIME_SECONDS = "s"
}