package me.blitzgamer_88.bountysystem.conf

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyBuilder
import ch.jalu.configme.properties.PropertyInitializer
import ch.jalu.configme.properties.types.PrimitivePropertyType

internal object Config : SettingsHolder {


    // SETTINGS

    @JvmField
    @Comment("Whether or not the plugin should check if the kill is one of the listed regions.")
    val useRegions: Property<Boolean> = PropertyInitializer.newProperty("useRegions", false)

    @JvmField
    @Comment("Whether or not the plugin should check if the kill is in one of the listed worlds.")
    val useWorlds: Property<Boolean> = PropertyInitializer.newProperty("useWorlds", false)

    @JvmField
    @Comment("List all regions where players should be able to claim bounties.")
    val enabledRegions: Property<MutableList<String>> = PropertyInitializer.newListProperty("enabled_regions", "warzone", "warzone2")

    @JvmField
    @Comment("List all worlds where players should be able to claim bounties.")
    val enabledWorlds: Property<MutableList<String>> = PropertyInitializer.newListProperty("enabled_worlds", "world", "world_the_end")

    @JvmField
    @Comment("How many bounties can a player set at a time.")
    val maxBountiesPerPlayer: Property<Int> = PropertyInitializer.newProperty("maxBountiesPerPlayer", 2)

    @JvmField
    @Comment("How much time does it take for a bounty to expire if no one kills the target.")
    val bountyExpiryTime: Property<Int> = PropertyInitializer.newProperty("bountyExpiryTime", 259200)

    @JvmField
    @Comment("Specify how many percents the server takes from the bounty. Make it 0 so the player that gets the kill takes all the money.")
    val bountyTax: Property<Int> = PropertyInitializer.newProperty("bountyTax", 5)

    @JvmField
    @Comment("Every how many minutes, the plugin checks to see if a bounty is expired.")
    val expiryTimeCheck: Property<Int> = PropertyInitializer.newProperty("runnableCoolDown", 5)

    // GUI
    @JvmField
    @Comment("/bounty GUI title. You can use any PAPI placeholder.")
    val guiTitle: Property<String> = PropertyInitializer.newProperty("guiTitle", "&cBounties")
    @JvmField
    @Comment("/bounty items title. You can use any PAPI placeholder.")
    val itemTitle: Property<String> = PropertyInitializer.newProperty("itemTitle", "&5Target: &c%player_name%")
    @JvmField
    @Comment("/bounty items lore. You can use the placeholders: %payer%, %amount%, %bountyId%. Also you can use any PAPI placeholder.")
    val itemLore: Property<MutableList<String>> = PropertyInitializer.newListProperty("itemLore", "", "&ePayer: &6%payer%", "&eBounty: &6%amount%$", "&eID: &6%bountyId%", "&eExpires in: &6%expiryTime%")


    // PERMISSIONS
    @JvmField
    val bountyOpenPermission: Property<String> = PropertyInitializer.newProperty("bountyOpenPermission", "bountysystem.open")
    @JvmField
    val bountyPlacePermission: Property<String> = PropertyInitializer.newProperty("bountyPlacePermission", "bountysystem.place")
    @JvmField
    val bountyAddPermission: Property<String> = PropertyInitializer.newProperty("bountyAddPermission", "bountysystem.add")
    @JvmField
    val bountyCancelPermission: Property<String> = PropertyInitializer.newProperty("bountyCancelPermission", "bountysystem.cancel")
    @JvmField
    val bountyByPassPermission: Property<String> = PropertyInitializer.newProperty("bountyByPassPermission", "bountysystem.bypass")
    @JvmField
    val adminBountyCancelPermission: Property<String> = PropertyInitializer.newProperty("adminBountyCancelPermission", "bountysystem.admin.cancel")
    @JvmField
    val adminBypassPermission: Property<String> = PropertyInitializer.newProperty("adminBypassPermission", "bountysystem.admin.bypass")
    @JvmField
    val adminReloadPermission: Property<String> = PropertyInitializer.newProperty("adminReloadPermission", "bountysystem.admin.reload")
    @JvmField
    val adminPermission: Property<String> = PropertyInitializer.newProperty("adminPermission", "bountysystem.admin")


    // MESSAGES
    @JvmField
    val playerNotFound: Property<String> = PropertyInitializer.newProperty("playerNotFound", "&7Could not find player. Make sure the player is online and you've typed the correct name.")
    @JvmField
    val notYourBounty: Property<String> = PropertyInitializer.newProperty("notYourBounty", "&7Bounty with id &c%bountyId%&7 was not placed by you.")
    @JvmField
    val bountyNotFound: Property<String> = PropertyInitializer.newProperty("bountyNotFound", "&7Could not find bounty with id &c%bountyId%&7.")
    @JvmField
    val configReloaded: Property<String> = PropertyInitializer.newProperty("configReloaded", "&aConfig Reloaded.")
    @JvmField
    val wrongUsage: Property<String> = PropertyInitializer.newProperty("wrongUsage", "&cWrong Usage.")
    @JvmField
    val noPermission: Property<String> = PropertyInitializer.newProperty("noPermission", "&cYou don't have permission for this.")
    @JvmField
    val targetHasBounty: Property<String> = PropertyInitializer.newProperty("targetHasBounty", "&7There is already a bounty on that player.")
    @JvmField
    val notEnoughMoney: Property<String> = PropertyInitializer.newProperty("notEnoughMoney", "&7You don't have enough money.")
    @JvmField
    val targetWhitelisted: Property<String> = PropertyInitializer.newProperty("targetWhitelisted", "&7You can't place a bounty on that player.")
    @JvmField
    val noBountiesFound: Property<String> = PropertyInitializer.newProperty("noBountiesFound", "&7There are no bounties at the moment.")
    @JvmField
    val bountyOnYourself: Property<String> = PropertyInitializer.newProperty("bountyOnYourself", "&7You can't place a bounty on yourself.")
    @JvmField
    val bountyPlacedSelf: Property<String> = PropertyInitializer.newProperty("bountyPlacedSelf", "&7You've placed a bounty on &a%target%&7 of &e%amount%\$&7. Bounty ID: &a%bountyId%.")
    @JvmField
    val bountyPlacedEveryone: Property<String> = PropertyInitializer.newProperty("bountyPlacedEveryone", "&a%player_name%&7 has placed a &e%amount%$&7 bounty on &a%target%&7's head!")
    @JvmField
    val bountyCanceled: Property<String> = PropertyInitializer.newProperty("bountyCanceled", "&7You have canceled the bounty with the id &c%bountyId%&7.")
    @JvmField
    val bountyCanceledByAdmin: Property<String> = PropertyInitializer.newProperty("bountyCanceledByAdmin", "&7An admin has canceled your bounty with the id &c%bountyId%&7.")
    @JvmField
    val playerWhitelisted: Property<String> = PropertyInitializer.newProperty("playerWhitelisted", "&7You have whitelisted &c%player_name%&7.")
    @JvmField
    val bountyReceived: Property<String> = PropertyInitializer.newProperty("bountyReceived", "&7You have killed %target% and collected a &e%amount%$&7 bounty.")
    @JvmField
    val maxBounties: Property<String> = PropertyInitializer.newProperty("maxBounties", "&7You have reached the maximum amounts of bounties you can place at a time.")
    @JvmField
    val bountyExpired: Property<String> = PropertyInitializer.newProperty("bountyExpired", "&7Your bounty has expired and no one claimed it.")
    @JvmField
    val bountyReceivedBroadcast: Property<String> = PropertyInitializer.newProperty("bountyReceivedBroadcast", "&c%player_name%&7 has killed &e%target%&7 and received &e%amount%$&7.")
    @JvmField
    val amountUpdated: Property<String> = PropertyInitializer.newProperty("amountUpdated", "&7Added another &e%newAmount%$ &7to the old amount of &e%oldAmount%$&7.")
}