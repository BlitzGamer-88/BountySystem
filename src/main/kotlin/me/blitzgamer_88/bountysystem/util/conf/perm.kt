package me.blitzgamer_88.bountysystem.util.conf

import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit.getServer


var perms: Permission? = null

fun setupPermissions(): Boolean {
    val rsp = getServer().servicesManager.getRegistration(Permission::class.java)
    perms = rsp!!.provider
    return perms != null
}