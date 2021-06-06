package com.blitzoffline.bountysystem.config

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Menu
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import me.mattstudios.config.SettingsManager
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit

class ConfigHandler(private val plugin: BountySystem) {
    fun loadConfig(): SettingsManager {
        val confFile = plugin.dataFolder.resolve("config.yml")
        if (!confFile.exists()) plugin.saveDefaultConfig()
        return SettingsManager
            .from(confFile)
            .configurationData(Bounties::class.java, Menu::class.java, Settings::class.java)
            .create()
    }

    fun loadMessages(): SettingsManager {
        val msgFile = plugin.dataFolder.resolve("config.yml")
        if (!msgFile.exists()) plugin.saveDefaultMessages()
        return SettingsManager
            .from(msgFile)
            .configurationData(Messages::class.java)
            .create()
    }

    fun loadEconomy(): Economy? {
        if (Bukkit.getServer().pluginManager.getPlugin("Vault") == null) return null
        val rsp = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java) ?: return null
        return rsp.provider
    }
}