package com.blitzoffline.bountysystem.database

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.bounty.Bounty
import com.blitzoffline.bountysystem.util.log
import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.Exception
import java.util.logging.Level

class Database(private val plugin: BountySystem) {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create()
    private var forceStopped = false

    fun load() {
        var count = 0
        try {
            "[BountySystem] Loading bounties...".log()
            val file = plugin.dataFolder.resolve("bounties.json")

            if (!file.exists()) plugin.dataFolder.resolve("bounties.json").createNewFile()
            if (file.length() == 0L) { "[BountySystem] Bounty file is empty. Loaded 0 bounties.".log(); return }

            val token = object : TypeToken<Collection<Bounty>>() {}.type
            val bounties: Collection<Bounty> = gson.fromJson(plugin.dataFolder.resolve("bounties.json").readText(), token)

            plugin.bountyHandler.BOUNTIES.clear()
            bounties.forEach { bounty ->
                if (!bounty.valid()) return@forEach
                if (plugin.bountyHandler.expired(bounty)) return@forEach
                plugin.bountyHandler.BOUNTIES.add(bounty)
                count++
            }
            if (count == 1) "[BountySystem] Found and loaded 1 bounty.".log()
            else "[BountySystem] Found and loaded $count bounties.".log()
        } catch (ex: Exception) {
            when (ex) {
                is JsonParseException, is JsonSyntaxException -> {
                    forceStopped = true
                    plugin.logger.log(Level.SEVERE, "Could not load the saved bounties due to a json syntax error in bounties.json.", ex)
                    plugin.pluginLoader.disablePlugin(plugin)
                }
                else -> throw ex
            }
        }

    }

    fun save() {
        try {
            if (forceStopped) return
            plugin.dataFolder.resolve("bounties.json").writeText(gson.toJson(plugin.bountyHandler.BOUNTIES))
        }
        catch (ex: JsonIOException) {
            plugin.logger.log(Level.SEVERE, "Could not save bounties!", ex)
        }
    }
}