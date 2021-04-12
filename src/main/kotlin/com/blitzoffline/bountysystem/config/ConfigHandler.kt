package com.blitzoffline.bountysystem.config

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Bounty
import com.blitzoffline.bountysystem.config.holder.GUI
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import me.mattstudios.config.SettingsManager

fun loadConfig(plugin: BountySystem) : SettingsManager {
    val file = plugin.dataFolder.resolve("config.yml")
    if (!file.exists()) plugin.saveDefaultConfig()
    return SettingsManager
        .from(file)
        .configurationData(Bounty::class.java, GUI::class.java, Settings::class.java)
        .create()
}

fun loadMessages(plugin: BountySystem) : SettingsManager {
    val file = plugin.dataFolder.resolve("messages.yml")
    if (!file.exists()) plugin.saveDefaultMessages()
    return SettingsManager
        .from(file)
        .configurationData(Messages::class.java)
        .create()
}