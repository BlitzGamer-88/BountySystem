package com.blitzoffline.bountysystem.database

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.bounty.Bounty
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.logging.Level

class Database(private val plugin: BountySystem) {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create()

    fun load() {
        try {
            val file = plugin.dataFolder.resolve("bounties.json")

            if (!file.exists()) plugin.dataFolder.resolve("bounties.json").createNewFile()
            if (file.length() == 0L) return

            val token = object : TypeToken<Collection<Bounty>>() {}.type
            val bounties: Collection<Bounty> = gson.fromJson(plugin.dataFolder.resolve("bounties.json").readText(), token)

            BOUNTIES_LIST.clear()

            var count = 0
            bounties.forEach {  bounty ->
                BOUNTIES_LIST[bounty.id.toString()] = bounty
                count++
            }
        }
        catch (ex: java.lang.Exception) {
            plugin.logger.log(Level.SEVERE, "Could not load the saved bounties!", ex)
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