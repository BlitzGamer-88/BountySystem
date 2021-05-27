package com.blitzoffline.bountysystem.config.holder

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Messages : SettingsHolder {

    @Path("NOT-YOUR-BOUNTY")
    val NOT_YOUR_BOUNTY = Property.create("&7Bounty with id &c%bountyId%&7 was not placed by you.")

    @Path("BOUNTY-NOT-FOUND")
    val BOUNTY_NOT_FOUND = Property.create("&7Could not find bounty with id &c%bountyId%&7.")

    @Path("CONFIG-RELOADED")
    val CONFIG_RELOADED = Property.create("&aConfig Reloaded.")

    @Path("WRONG-USAGE")
    val WRONG_USAGE = Property.create("&cWrong Usage.")

    @Path("NO-PERMISSION")
    val NO_PERMISSION = Property.create("&cYou don't have permission for this.")

    @Path("NOT-ENOUGH-MONEY")
    val NOT_ENOUGH_MONEY = Property.create("&7You don't have enough money.")

    @Path("TARGET-WHITELISTED")
    val TARGET_WHITELISTED = Property.create("&7You can't place a bounty on that player.")

    @Path("NO-BOUNTIES-FOUND")
    val NO_BOUNTIES_FOUND = Property.create("&7There are no bounties at the moment.")

    @Path("BOUNTY-ON-YOURSELF")
    val BOUNTY_ON_YOURSELF = Property.create("&7You can't place a bounty on yourself.")

    @Path("BOUNTY-PLACED-SELF")
    val BOUNTY_PLACED_SELF = Property.create("&7You've placed a bounty on &a%target%&7 of &e%amount%\$&7. Bounty ID: &a%bountyId%.")

    @Path("BOUNTY-PLACED-EVERYONE")
    val BOUNTY_PLACED_EVERYONE = Property.create("&a%player_name%&7 has placed a &e%amount%$&7 bounty on &a%target%&7's head!")

    @Path("BOUNTY-CANCELED")
    val BOUNTY_CANCELED = Property.create("&7You have canceled the bounty with the id &c%bountyId%&7.")

    @Path("BOUNTY-CANCELED-BY-ADMIN")
    val BOUNTY_CANCELED_ADMIN = Property.create("&7An admin has canceled your bounty with the id &c%bountyId%&7.")

    @Path("PLAYER-WHITELISTED")
    val PLAYER_WHITELISTED = Property.create("&7You have whitelisted &c%player_name%&7.")

    @Path("BOUNTY-RECEIVED")
    val BOUNTY_RECEIVED = Property.create("&7You have killed %target% and collected a &e%amount%$&7 bounty.")

    @Path("MAX-BOUNTIES")
    val MAX_BOUNTIES = Property.create("&7You have reached the maximum amounts of bounties you can place at a time.")

    @Path("BOUNTY-EXPIRED")
    val BOUNTY_EXPIRED = Property.create("&7Your bounty has expired and no one claimed it.")

    @Path("BOUNTY-RECEIVED-BROADCAST")
    val BOUNTY_RECEIVED_BROADCAST = Property.create("&c%player_name%&7 has killed &e%target%&7 and received &e%amount%$&7.")

    @Path("AMOUNT-UPDATED")
    val AMOUNT_UPDATED = Property.create("&7Added another &e%newAmount%$ &7to the old amount of &e%oldAmount%$&7.")

    @Path("TIME-FORMAT.days")
    val TIME_DAYS = Property.create("d ")
    @Path("TIME-FORMAT.hours")
    val TIME_HOURS = Property.create("h ")
    @Path("TIME-FORMAT.minutes")
    val TIME_MINUTES = Property.create("m ")
    @Path("TIME-FORMAT.seconds")
    val TIME_SECONDS = Property.create("s")

}