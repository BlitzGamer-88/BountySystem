package com.blitzoffline.bountysystem.config.sections

import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newListProperty
import ch.jalu.configme.properties.PropertyInitializer.newProperty

object Settings : SettingsHolder {
    @JvmField
    val useRegions: Property<Boolean> = newProperty("use-regions", false)
    @JvmField
    val useWorlds: Property<Boolean> = newProperty("use-worlds", false)
    @JvmField
    val enabledRegions: Property<MutableList<String>> = newListProperty("enabled_regions", "warzone", "warzone2")
    @JvmField
    val enabledWorlds: Property<MutableList<String>> = newListProperty("enabled_worlds", "world", "world_the_end")
    @JvmField
    val maxBountiesPerPlayer: Property<Int> = newProperty("max-bounties", 2)
    @JvmField
    val bountyExpiryTime: Property<Int> = newProperty("bounty-expiry-time", 259200)
    @JvmField
    val bountyTax: Property<Int> = newProperty("bounty-tax", 5)
    @JvmField
    val expiryCheckInterval: Property<Int> = newProperty("expiry-check-interval", 300)
    @JvmField
    val cacheSaveInterval: Property<Int> = newProperty("cache-save-interval", 300)

    @JvmField
    val daysFormat: Property<String> = newProperty("time-format.days", "d ")
    @JvmField
    val hoursFormat: Property<String> = newProperty("time-format.hours", "h ")
    @JvmField
    val minutesFormat: Property<String> = newProperty("time-format.minutes", "m ")
    @JvmField
    val secondsFormat: Property<String> = newProperty("time-format.seconds", "s")

    @JvmField
    val fillerItemMaterial: Property<String> = newProperty("filler-item.material", "BLACK_STAINED_GLASS_PANE")
    @JvmField
    val fillerItemName: Property<String> = newProperty("filler-item.name", " ")
    @JvmField
    val nextPagName: Property<String> = newProperty("next-page-name", "&6Next")
    @JvmField
    val previousPageName: Property<String> = newProperty("previous-page-name", "&6Previous")
    @JvmField
    val guiTitle: Property<String> = newProperty("gui-title", "&cBounties")
    @JvmField
    val itemName: Property<String> = newProperty("item-name", "&5Target: &c%player_name%")
    @JvmField
    val itemLore: Property<MutableList<String>> = newListProperty("item-lore", "", "&ePayer: &6%payer%", "&eBounty: &6%amount%$", "&eID: &6%bountyId%", "&eExpires in: &6%expiryTime%")
}