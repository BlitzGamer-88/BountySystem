package me.blitzgamer_88.bountysystem.util.conf

import ch.jalu.configme.SettingsManager
import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.conf.BountySystemConfiguration

const val minId = 1000
const val maxId = 9999

private var conf = null as? SettingsManager?

fun loadConfig(plugin: BountySystem) {
    val file = plugin.dataFolder.resolve("config.yml")
    if (!file.exists()) {
        file.parentFile.mkdirs()
        file.createNewFile()
    }
    conf = BountySystemConfiguration(file)
}

fun conf(): SettingsManager {
    return checkNotNull(conf)
}