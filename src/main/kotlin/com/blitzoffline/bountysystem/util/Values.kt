package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.config.holder.Settings
import org.bukkit.Material

const val minId = 1000
const val maxId = 9999

var useRegions = false
var useWorlds = false

lateinit var enabledRegions: MutableList<String>
lateinit var enabledWorlds: MutableList<String>

var maxBountiesPerPlayer = 0
var bountyExpiryTime = 259200
var bountyTax = 5
var expiryCheckInterval = 300
var cacheSaveInterval = 300

lateinit var fillerItemMaterial: Material
lateinit var fillerItemName: String
lateinit var guiTitle: String
lateinit var nextPagName: String
lateinit var previousPageName: String
lateinit var itemName: String
lateinit var itemLore: MutableList<String>

lateinit var daysFormat: String
lateinit var hoursFormat: String
lateinit var minutesFormat: String
lateinit var secondsFormat: String

fun registerSettings() {
    useRegions = conf.getProperty(Settings.useRegions)
    useWorlds = conf.getProperty(Settings.useWorlds)

    enabledRegions = conf.getProperty(Settings.enabledRegions)
    enabledWorlds = conf.getProperty(Settings.enabledWorlds)

    maxBountiesPerPlayer = conf.getProperty(Settings.maxBountiesPerPlayer)
    bountyExpiryTime = conf.getProperty(Settings.bountyExpiryTime)
    bountyTax = conf.getProperty(Settings.bountyTax)
    expiryCheckInterval = conf.getProperty(Settings.expiryCheckInterval)
    cacheSaveInterval = conf.getProperty(Settings.cacheSaveInterval)


    fillerItemMaterial = Material.valueOf(conf.getProperty(Settings.fillerItemMaterial))
    fillerItemName = conf.getProperty(Settings.fillerItemName).color()
    nextPagName = conf.getProperty(Settings.nextPagName).color()
    previousPageName = conf.getProperty(Settings.previousPageName).color()
    guiTitle = conf.getProperty(Settings.guiTitle).color()
    itemName = conf.getProperty(Settings.itemName).color()
    itemLore = conf.getProperty(Settings.itemLore).color()

    daysFormat = conf.getProperty(Settings.daysFormat).color()
    hoursFormat = conf.getProperty(Settings.hoursFormat).color()
    minutesFormat = conf.getProperty(Settings.minutesFormat).color()
    secondsFormat = conf.getProperty(Settings.secondsFormat).color()
}

lateinit var playerNotFound: String
lateinit var notYourBounty: String
lateinit var bountyNotFound: String
lateinit var configReloaded: String
lateinit var wrongUsage: String
lateinit var noPermission: String
lateinit var notEnoughMoney: String
lateinit var targetWhitelisted: String
lateinit var noBountiesFound: String
lateinit var bountyOnYourself: String
lateinit var bountyPlacedSelf: String
lateinit var bountyPlacedEveryone: String
lateinit var bountyCanceled: String
lateinit var bountyCanceledByAdmin: String
lateinit var playerWhitelisted: String
lateinit var bountyReceived: String
lateinit var maxBounties: String
lateinit var bountyExpired: String
lateinit var bountyReceivedBroadcast: String
lateinit var amountUpdated: String

fun registerMessages() {
    playerNotFound = msg.getProperty(Messages.playerNotFound).color()
    notYourBounty = msg.getProperty(Messages.notYourBounty).color()
    bountyNotFound = msg.getProperty(Messages.bountyNotFound).color()
    configReloaded = msg.getProperty(Messages.configReloaded).color()
    wrongUsage = msg.getProperty(Messages.wrongUsage).color()
    noPermission = msg.getProperty(Messages.noPermission).color()
    notEnoughMoney = msg.getProperty(Messages.notEnoughMoney).color()
    targetWhitelisted = msg.getProperty(Messages.targetWhitelisted).color()
    noBountiesFound = msg.getProperty(Messages.noBountiesFound).color()
    bountyOnYourself = msg.getProperty(Messages.bountyOnYourself).color()
    bountyPlacedSelf = msg.getProperty(Messages.bountyPlacedSelf).color()
    bountyPlacedEveryone = msg.getProperty(Messages.bountyPlacedEveryone).color()
    bountyCanceled = msg.getProperty(Messages.bountyCanceled).color()
    bountyCanceledByAdmin = msg.getProperty(Messages.bountyCanceledByAdmin).color()
    playerWhitelisted = msg.getProperty(Messages.playerWhitelisted).color()
    bountyReceived = msg.getProperty(Messages.bountyReceived).color()
    maxBounties = msg.getProperty(Messages.maxBounties).color()
    bountyExpired = msg.getProperty(Messages.bountyExpired).color()
    bountyReceivedBroadcast = msg.getProperty(Messages.bountyReceivedBroadcast).color()
    amountUpdated = msg.getProperty(Messages.amountUpdated).color()
}