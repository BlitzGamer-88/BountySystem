package com.blitzoffline.bountysystem.util

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit

lateinit var econ: Economy
lateinit var perms: Permission

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