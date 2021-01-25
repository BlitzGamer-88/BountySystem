package me.blitzgamer_88.bountysystem.database

import com.google.gson.reflect.TypeToken
import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.bounty.Bounty
import java.io.File
import java.util.logging.Level

class Database(private val plugin: BountySystem) {
    private val gson = BountySystem.GSON

    fun load() {
        try {
            if (!File("bounties.json").exists()) plugin.dataFolder.resolve("bounties.json").createNewFile()
            if (File("bounties.json").length() == 0L) return
            val bounties: Collection<Bounty> = gson.fromJson(plugin.dataFolder.resolve("bounties.json").readText(), object : TypeToken<Collection<Bounty>>() {}.type)
            plugin.BOUNTIES_LIST.clear()
            for (bounty in bounties) {
                plugin.BOUNTIES_LIST[bounty.id.toString()] = bounty
            }
        }
        catch (ex: Exception) {
            plugin.logger.log(Level.SEVERE, "Could not load bounties!", ex)
        }
    }

    fun save() {
        try {
            plugin.dataFolder.resolve("bounties.json").writeText(gson.toJson(plugin.BOUNTIES_LIST.values))
        }
        catch (ex: Exception) {
            plugin.logger.log(Level.SEVERE, "Could not save bounties!", ex)
        }
    }
}