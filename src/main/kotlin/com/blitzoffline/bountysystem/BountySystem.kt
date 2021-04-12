package com.blitzoffline.bountysystem

import com.blitzoffline.bountysystem.database.Database
import com.blitzoffline.bountysystem.command.CommandBountySystem
import com.blitzoffline.bountysystem.command.CommandBountySystemAdmin
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import com.blitzoffline.bountysystem.config.loadConfig
import com.blitzoffline.bountysystem.config.loadMessages
import com.blitzoffline.bountysystem.listener.PlayerDeathListener
import com.blitzoffline.bountysystem.runnable.BountyExpire
import com.blitzoffline.bountysystem.runnable.SaveCache
import com.blitzoffline.bountysystem.util.*
import me.mattstudios.config.SettingsManager
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mf.base.CommandManager
import me.mattstudios.mf.base.components.CompletionResolver
import me.mattstudios.mf.base.components.MessageResolver
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.io.File

class BountySystem : JavaPlugin() {
    lateinit var config: SettingsManager
    lateinit var messages: SettingsManager

    private lateinit var commandManager: CommandManager
    lateinit var database: Database
        private set

    private lateinit var BOUNTY_EXPIRATION: BukkitTask
    private lateinit var CACHE_SAVING: BukkitTask

    override fun onEnable() {
        config = loadConfig(this)
        messages = loadMessages(this)

        database = Database(this)

        if (!setupPAPI()) { "[BountySystem] Could not find PlaceholderAPI! This plugin is required".log() }
        if (!setupEconomy()) { "[BountySystem] Could not find Vault! This plugin is required".log() }
        if (!setupPermissions()) { "[BountySystem] Could not find Vault! This plugin is required".log() }

        registerListeners(
            PlayerDeathListener(this)
        )

        commandManager = CommandManager(this, true)
        registerMessage("cmd.no.permission") { sender -> messages[Messages.NO_PERMISSION].msg(sender) }
        registerMessage("cmd.wrong.usage") { sender -> messages[Messages.WRONG_USAGE].msg(sender) }
        registerCompletion("#id") { listOf("<bountyID>") }
        registerCompletion("#amount") { listOf("<amount>") }
        registerCommands(
            CommandBountySystem(this),
            CommandBountySystemAdmin(this)
        )

        database.load()
        registerTasks()

        "[BountySystem] Plugin enabled successfully!".log()
    }

    override fun onDisable() {
        database.save()
        "[BountySystem] Plugin disabled successfully!".log()
    }

    fun reload() {
        database.save()
        config.reload()
        messages.reload()
        registerTasks()
    }

    private fun registerTasks() {
        if(!CACHE_SAVING.isCancelled) CACHE_SAVING.cancel()
        if(!BOUNTY_EXPIRATION.isCancelled) BOUNTY_EXPIRATION.cancel()

        CACHE_SAVING = SaveCache(this).runTaskTimer(this, config[Settings.INTERVAL_CACHE] * 20L, config[Settings.INTERVAL_CACHE] * 20L)
        BOUNTY_EXPIRATION = BountyExpire(this).runTaskTimer(this, config[Settings.INTERVAL_EXPIRY] * 20L, config[Settings.INTERVAL_EXPIRY] * 20L)
    }

    private fun registerCommands(vararg commands: CommandBase) = commands.forEach(commandManager::register)
    private fun registerCompletion(completionId: String, resolver: CompletionResolver) = commandManager.completionHandler.register(completionId, resolver)
    private fun registerMessage(messageId: String, resolver: MessageResolver) = commandManager.messageHandler.register(messageId, resolver)
    private fun registerListeners(vararg listeners: Listener) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

    fun saveDefaultMessages() {
        if (File("messages.yml").exists()) return
        saveResource("messages.yml", false)
    }
}