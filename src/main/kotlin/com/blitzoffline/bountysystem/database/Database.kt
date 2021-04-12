package com.blitzoffline.bountysystem.database

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.bounty.Bounty
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.logging.Level

class Database(private val plugin: BountySystem) {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create()

    fun load() {
        try {
            if (!File("bounties.json").exists()) plugin.dataFolder.resolve("bounties.json").createNewFile()
            if (File("bounties.json").length() == 0L) return

            val token = object : TypeToken<Collection<Bounty>>() {}.type
            val bounties: Collection<Bounty> = gson.fromJson(plugin.dataFolder.resolve("bounties.json").readText(), token)

            BOUNTIES_LIST.clear()

            bounties.forEach {  bounty ->
                    BOUNTIES_LIST[bounty.id.toString()] = bounty
                }
        }
        catch (ex: java.lang.Exception) {
            plugin.logger.log(Level.SEVERE, "Could not load bounties!", ex)
        }
    }

    fun save() {
        try {
            plugin.dataFolder.resolve("bounties.json").writeText(gson.toJson(BOUNTIES_LIST.values))
        }
        catch (ex: java.lang.Exception) {
            plugin.logger.log(Level.SEVERE, "Could not save bounties!", ex)
        }
    }
}