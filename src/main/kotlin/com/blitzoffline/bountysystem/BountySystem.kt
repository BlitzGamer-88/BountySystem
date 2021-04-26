package com.blitzoffline.bountysystem

import com.blitzoffline.bountysystem.command.CommandBountySystem
import com.blitzoffline.bountysystem.command.CommandBountySystemAdmin
import com.blitzoffline.bountysystem.config.*
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import com.blitzoffline.bountysystem.database.Database
import com.blitzoffline.bountysystem.listener.PlayerDeathListener
import com.blitzoffline.bountysystem.placeholders.BountyPlaceholders
import com.blitzoffline.bountysystem.runnable.BountyExpire
import com.blitzoffline.bountysystem.runnable.SaveCache
import com.blitzoffline.bountysystem.util.adventure
import com.blitzoffline.bountysystem.util.log
import com.blitzoffline.bountysystem.util.msg
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mf.base.CommandManager
import me.mattstudios.mf.base.components.CompletionResolver
import me.mattstudios.mf.base.components.MessageResolver
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.io.File

class BountySystem : JavaPlugin() {
    private lateinit var commandManager: CommandManager
    lateinit var database: Database
        private set

    private lateinit var BOUNTY_EXPIRATION: BukkitTask
    private lateinit var CACHE_SAVING: BukkitTask

    override fun onEnable() {
        loadConfig(this)
        loadMessages(this)

        database = Database(this)
        adventure = BukkitAudiences.create(this)

        if (!setupPAPI()) { "[BountySystem] Could not find PlaceholderAPI! This plugin is required".log(); pluginLoader.disablePlugin(this) }
        if (!setupEconomy()) { "[BountySystem] Could not find Vault! This plugin is required".log(); pluginLoader.disablePlugin(this) }
        if (!setupPermissions()) { "[BountySystem] Could not find Vault! This plugin is required".log(); pluginLoader.disablePlugin(this) }
        if (!setupWG() && settings[Settings.REGIONS_USE]) { "[BountySystem] Could not find WorldGuard! Disable regions.use if you don't want to use WorldGuard".log(); pluginLoader.disablePlugin(this) }

        BountyPlaceholders(this).register()

        registerListeners(
            PlayerDeathListener()
        )

        commandManager = CommandManager(this, true)
        registerMessage("cmd.no.permission") { sender -> messages[Messages.NO_PERMISSION].msg(sender) }
        registerMessage("cmd.wrong.usage") { sender -> messages[Messages.WRONG_USAGE].msg(sender) }
        registerCompletion("#id") { listOf("<bountyID>") }
        registerCompletion("#amount") { listOf("<amount>") }
        registerCommands(
            CommandBountySystem(),
            CommandBountySystemAdmin(this)
        )

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
        registerTasks()
    }

    private fun registerTasks() {
        if(::CACHE_SAVING.isInitialized && !CACHE_SAVING.isCancelled) CACHE_SAVING.cancel()
        if(::BOUNTY_EXPIRATION.isInitialized && !BOUNTY_EXPIRATION.isCancelled) BOUNTY_EXPIRATION.cancel()

        CACHE_SAVING = SaveCache(this).runTaskTimer(this, settings[Settings.INTERVAL_CACHE] * 20L, settings[Settings.INTERVAL_CACHE] * 20L)
        BOUNTY_EXPIRATION = BountyExpire().runTaskTimer(this, settings[Settings.INTERVAL_EXPIRY] * 20L, settings[Settings.INTERVAL_EXPIRY] * 20L)
    }

    private fun setupPAPI(): Boolean {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null
    }

    private fun setupWG(): Boolean {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null
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