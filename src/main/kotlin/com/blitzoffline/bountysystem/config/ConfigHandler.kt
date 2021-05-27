package com.blitzoffline.bountysystem.config

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Menu
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import me.mattstudios.config.SettingsManager
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit

lateinit var settings: SettingsManager
lateinit var messages: SettingsManager
lateinit var econ: Economy

fun loadConfig(plugin: BountySystem) {
    val file = plugin.dataFolder.resolve("config.yml")
    if (!file.exists()) plugin.saveDefaultConfig()
    settings = SettingsManager
        .from(file)
        .configurationData(Bounties::class.java, Menu::class.java, Settings::class.java)
        .create()
}

fun loadMessages(plugin: BountySystem) {
    val file = plugin.dataFolder.resolve("messages.yml")
    if (!file.exists()) plugin.saveDefaultMessages()
    messages =  SettingsManager
        .from(file)
        .configurationData(Messages::class.java)
        .create()
}

fun setupEconomy(): Boolean {
    if (Bukkit.getServer().pluginManager.getPlugin("Vault") == null) return false
    val rsp = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java) ?: return false
    econ = rsp.provider
    return true
}