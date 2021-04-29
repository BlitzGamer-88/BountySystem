package com.blitzoffline.bountysystem

import com.blitzoffline.bountysystem.command.CommandBountySystem
import com.blitzoffline.bountysystem.command.CommandBountySystemAdmin
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import com.blitzoffline.bountysystem.config.loadConfig
import com.blitzoffline.bountysystem.config.loadMessages
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.config.settings
import com.blitzoffline.bountysystem.config.setupEconomy
import com.blitzoffline.bountysystem.config.setupPermissions
import com.blitzoffline.bountysystem.database.Database
import com.blitzoffline.bountysystem.listener.PlayerDeathListener
import com.blitzoffline.bountysystem.placeholders.BountyPlaceholders
import com.blitzoffline.bountysystem.task.BountyExpire
import com.blitzoffline.bountysystem.task.SaveDataTask
import com.blitzoffline.bountysystem.util.adventure
import com.blitzoffline.bountysystem.util.log
import com.blitzoffline.bountysystem.util.msg
import me.mattstudios.mf.base.CommandManager
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class BountySystem : JavaPlugin() {
    private lateinit var commandManager: CommandManager
    private lateinit var updateBounties: BukkitTask
    private lateinit var saveData: BukkitTask
    lateinit var database: Database

    override fun onEnable() {
        loadConfig(this)
        loadMessages(this)

        database = Database(this)
        adventure = BukkitAudiences.create(this)

        if (!setupHooks()) pluginLoader.disablePlugin(this)
        BountyPlaceholders(this).register()
        registerListeners(
            PlayerDeathListener()
        )

        commandManager = CommandManager(this, true)
        with (commandManager.messageHandler) {
            register("cmd.wrong.usage") { sender -> messages[Messages.WRONG_USAGE].msg(sender) }
            register("cmd.no.permission") { sender -> messages[Messages.NO_PERMISSION].msg(sender) }
        }
        with (commandManager.completionHandler) {
            register("#id") { listOf("<bountyID>") }
            register("#amount") { listOf("<amount>") }
        }
        with (commandManager) {
            register(CommandBountySystem())
            register(CommandBountySystemAdmin(this@BountySystem))
        }

        registerTasks()
        database.loadBounties()
        "[BountySystem] Plugin enabled successfully!".log()
    }

    override fun onDisable() {
        database.saveBounties()
        "[BountySystem] Plugin disabled successfully!".log()
    }

    fun reload() {
        database.saveBounties()
        settings.reload()
        messages.reload()
    }

    private fun registerTasks() {
        if(::saveData.isInitialized && !saveData.isCancelled) saveData.cancel()
        if(::updateBounties.isInitialized && !updateBounties.isCancelled) updateBounties.cancel()

        saveData = SaveDataTask(this).runTaskTimer(this, settings[Settings.INTERVAL_CACHE] * 20L, settings[Settings.INTERVAL_CACHE] * 20L)
        updateBounties = BountyExpire().runTaskTimer(this, settings[Settings.INTERVAL_EXPIRY] * 20L, settings[Settings.INTERVAL_EXPIRY] * 20L)
    }

    private fun setupHooks(): Boolean {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            "[BountySystem] Could not find PlaceholderAPI! This plugin is required".log()
            return false
        }
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null && settings[Settings.REGIONS_USE]) {
            "[BountySystem] Could not find WorldGuard! Disable regions.use if you don't want to use WorldGuard".log()
            return false
        }
        if (!setupEconomy()) {
            "[BountySystem] Could not find Vault! This plugin is required".log()
            return false
        }
        if (!setupPermissions()) {
            "[BountySystem] Could not find Vault! This plugin is required".log()
            return false
        }
        return true
    }

    private fun registerListeners(vararg listeners: Listener) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

    fun saveDefaultMessages() {
        if (dataFolder.resolve("messages.yml").exists()) return
        saveResource("messages.yml", false)
    }
}