package com.blitzoffline.bountysystem.database

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.bounty.Bounty
import com.blitzoffline.bountysystem.util.log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
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

            BOUNTIES_LIST.clear()

            bounties.forEach {  bounty ->
                if (BOUNTIES_LIST[bounty.id.toString()] == null) count++
                BOUNTIES_LIST[bounty.id.toString()] = bounty
            }
            if (bounties.size == count) "[BountySystem] Found and loaded $count bounties.".log()
            else "[BountySystem] Found ${bounties.size} bounties but could only load $count of them.".log()
        }
        catch (ex: java.lang.Exception) {
            forceStopped = true
            plugin.logger.log(Level.SEVERE, "Could not load the saved bounties!", ex)
            plugin.pluginLoader.disablePlugin(plugin)
        }
    }

    fun save() {
        try {
            if (forceStopped) return
            plugin.dataFolder.resolve("bounties.json").writeText(gson.toJson(BOUNTIES_LIST.values))
        }
        catch (ex: java.lang.Exception) {
            plugin.logger.log(Level.SEVERE, "Could not save bounties!", ex)
        }
    }
}