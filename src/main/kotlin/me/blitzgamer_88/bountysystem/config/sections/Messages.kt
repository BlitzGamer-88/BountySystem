package me.blitzgamer_88.bountysystem.config.sections

import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty

object Messages : SettingsHolder {
    @JvmField
    val playerNotFound: Property<String> = newProperty("PLAYER-NOT-FOUND", "&7Could not find player. Make sure the player is online and you've typed the correct name.")
    @JvmField
    val notYourBounty: Property<String> = newProperty("NOT-YOUR-BOUNTY", "&7Bounty with id &c%bountyId%&7 was not placed by you.")
    @JvmField
    val bountyNotFound: Property<String> = newProperty("BOUNTY-NOT-FOUND", "&7Could not find bounty with id &c%bountyId%&7.")
    @JvmField
    val configReloaded: Property<String> = newProperty("CONFIG-RELOADED", "&aConfig Reloaded.")
    @JvmField
    val wrongUsage: Property<String> = newProperty("WRONG-USAGE", "&cWrong Usage.")
    @JvmField
    val noPermission: Property<String> = newProperty("NO-PERMISSION", "&cYou don't have permission for this.")
    @JvmField
    val notEnoughMoney: Property<String> = newProperty("NOT-ENOUGH-MONEY", "&7You don't have enough money.")
    @JvmField
    val targetWhitelisted: Property<String> = newProperty("TARGET-WHITELISTED", "&7You can't place a bounty on that player.")
    @JvmField
    val noBountiesFound: Property<String> = newProperty("NO-BOUNTIES-FOUND", "&7There are no bounties at the moment.")
    @JvmField
    val bountyOnYourself: Property<String> = newProperty("BOUNTY-ON-YOURSELF", "&7You can't place a bounty on yourself.")
    @JvmField
    val bountyPlacedSelf: Property<String> = newProperty("BOUNTY-PLACED-SELF", "&7You've placed a bounty on &a%target%&7 of &e%amount%\$&7. Bounty ID: &a%bountyId%.")
    @JvmField
    val bountyPlacedEveryone: Property<String> = newProperty("BOUNTY-PLACED-EVERYONE", "&a%player_name%&7 has placed a &e%amount%$&7 bounty on &a%target%&7's head!")
    @JvmField
    val bountyCanceled: Property<String> = newProperty("BOUNTY-CANCELED", "&7You have canceled the bounty with the id &c%bountyId%&7.")
    @JvmField
    val bountyCanceledByAdmin: Property<String> = newProperty("BOUNTY-CANCELED-BY-ADMIN", "&7An admin has canceled your bounty with the id &c%bountyId%&7.")
    @JvmField
    val playerWhitelisted: Property<String> = newProperty("PLAYER-WHITELISTED", "&7You have whitelisted &c%player_name%&7.")
    @JvmField
    val bountyReceived: Property<String> = newProperty("BOUNTY-RECEIVED", "&7You have killed %target% and collected a &e%amount%$&7 bounty.")
    @JvmField
    val maxBounties: Property<String> = newProperty("MAX-BOUNTIES", "&7You have reached the maximum amounts of bounties you can place at a time.")
    @JvmField
    val bountyExpired: Property<String> = newProperty("BOUNTY-EXPIRED", "&7Your bounty has expired and no one claimed it.")
    @JvmField
    val bountyReceivedBroadcast: Property<String> = newProperty("BOUNTY-RECEIVED-BROADCAST", "&c%player_name%&7 has killed &e%target%&7 and received &e%amount%$&7.")
    @JvmField
    val amountUpdated: Property<String> = newProperty("AMOUNT-UPDATED", "&7Added another &e%newAmount%$ &7to the old amount of &e%oldAmount%$&7.")
}