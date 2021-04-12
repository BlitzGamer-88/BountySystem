package com.blitzoffline.bountysystem.util

import ch.jalu.configme.SettingsManager
import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.BountySystemMessages
import com.blitzoffline.bountysystem.config.BountySystemSettings
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit

lateinit var conf: SettingsManager
lateinit var msg: SettingsManager
lateinit var econ: Economy
lateinit var perms: Permission

fun loadSettings(plugin: BountySystem) {
    val file = plugin.dataFolder.resolve("config.yml")
    if (!file.exists()) {
        plugin.saveDefaultConfig()
    }
    conf = BountySystemSettings(file)
}

fun loadMessages(plugin: BountySystem) {
    val file = plugin.dataFolder.resolve("messages.yml")
    if (!file.exists()) {
        plugin.saveDefaultMessages()
    }
    msg = BountySystemMessages(file)
}

fun setupPAPI(): Boolean {
    return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null
}

fun setupEconomy(): Boolean {
    if (Bukkit.getServer().pluginManager.getPlugin("Vault") == null) return false
    val rsp = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java) ?: return false
    econ = rsp.provider
    return true
}

fun setupPermissions(): Boolean {
    val rsp = Bukkit.getServer().servicesManager.getRegistration(Permission::class.java) ?: return false
    perms = rsp.provider
    return true
}