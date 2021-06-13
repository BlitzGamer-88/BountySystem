package com.blitzoffline.bountysystem

import com.blitzoffline.bountysystem.bounty.BountyHandler
import com.blitzoffline.bountysystem.command.CommandAdminCancel
import com.blitzoffline.bountysystem.command.CommandAdminReload
import com.blitzoffline.bountysystem.command.CommandBounty
import com.blitzoffline.bountysystem.command.CommandBountyCancel
import com.blitzoffline.bountysystem.command.CommandBountyHelp
import com.blitzoffline.bountysystem.command.CommandBountyIncrease
import com.blitzoffline.bountysystem.command.CommandBountyPlace
import com.blitzoffline.bountysystem.config.ConfigHandler
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.holder.Settings
import com.blitzoffline.bountysystem.database.Database
import com.blitzoffline.bountysystem.listener.PlayerDeathListener
import com.blitzoffline.bountysystem.placeholders.BountyPlaceholders
import com.blitzoffline.bountysystem.task.BountyExpire
import com.blitzoffline.bountysystem.task.SaveDataTask
import com.blitzoffline.bountysystem.util.sendMessage
import java.util.logging.Level
import me.mattstudios.config.SettingsManager
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mf.base.CommandManager
import me.mattstudios.mf.base.components.CompletionResolver
import me.mattstudios.mf.base.components.MessageResolver
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask


class BountySystem : JavaPlugin() {
    private lateinit var commandManager: CommandManager
    private lateinit var configHandler: ConfigHandler
    private lateinit var updateBounties: BukkitTask
    private lateinit var saveData: BukkitTask

    lateinit var bountyHandler: BountyHandler
        private set
    lateinit var database: Database
        private set

    lateinit var settings: SettingsManager
        private set
    lateinit var messages: SettingsManager
        private set
    lateinit var economy: Economy
        private set

    override fun onEnable() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig")
        } catch (ignored: ClassNotFoundException) {
            logger.log(Level.INFO, "&cTHIS PLUGIN SHOULD BE USED ON PAPER: papermc.io/download")
        }

        configHandler = ConfigHandler(this)
        settings = configHandler.loadConfig()
        messages = configHandler.loadMessages()
        economy = configHandler.loadEconomy() ?: run {
            logger.log(Level.SEVERE, "[BountySystem] Could not find Vault! This plugin is required")
            pluginLoader.disablePlugin(this)
            return
        }

        bountyHandler = BountyHandler(this)

        database = Database(this)
        database.load()

        if (!setupHooks()) pluginLoader.disablePlugin(this)
        BountyPlaceholders(this).register()
        registerListeners(
            PlayerDeathListener(this)
        )

        commandManager = CommandManager(this, true)

        registerMessage("cmd.wrong.usage") { messages[Messages.WRONG_USAGE].sendMessage(it) }
        registerMessage("cmd.no.permission") { messages[Messages.NO_PERMISSION].sendMessage(it) }

        registerCompletion("#id") { listOf("<bountyId>") }
        registerCompletion("#amount") { listOf("<amount>") }

        registerCommands(
            CommandAdminCancel(this),
            CommandAdminReload(this),
            CommandBounty(this),
            CommandBountyCancel(this),
            CommandBountyHelp(),
            CommandBountyIncrease(this),
            CommandBountyPlace(this)
        )

        registerTasks()
        logger.log(Level.INFO, "[BountySystem] Plugin enabled successfully!")
    }

    override fun onDisable() {
        database.save()
        logger.log(Level.INFO, "[BountySystem] Plugin disabled successfully!")
    }

    private fun registerTasks() {
        if(::saveData.isInitialized && !saveData.isCancelled) saveData.cancel()
        if(::updateBounties.isInitialized && !updateBounties.isCancelled) updateBounties.cancel()

        saveData = SaveDataTask(this).runTaskTimer(this, settings[Settings.INTERVAL_CACHE] * 20L, settings[Settings.INTERVAL_CACHE] * 20L)
        updateBounties = BountyExpire(this).runTaskTimerAsynchronously(this, settings[Settings.INTERVAL_EXPIRY] * 20L, settings[Settings.INTERVAL_EXPIRY] * 20L)
    }

    private fun setupHooks(): Boolean {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            logger.log(Level.SEVERE, "[BountySystem] Could not find PlaceholderAPI! This plugin is required")
            return false
        }
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null && settings[Settings.REGIONS_USE]) {
            logger.log(Level.SEVERE, "[BountySystem] Could not find WorldGuard! Disable regions.use if you don't want to use WorldGuard")
            return false
        }
        return true
    }

    private fun registerListeners(vararg listeners: Listener) = listeners.forEach { server.pluginManager.registerEvents(it, this) }
    private fun registerCommands(vararg commands: CommandBase) = commands.forEach(commandManager::register)
    private fun registerCompletion(completionId: String, resolver: CompletionResolver) = commandManager.completionHandler.register(completionId, resolver)
    private fun registerMessage(messageId: String, resolver: MessageResolver) = commandManager.messageHandler.register(messageId, resolver)

    fun saveDefaultMessages() {
        if (dataFolder.resolve("messages.yml").exists()) return
        saveResource("messages.yml", false)
    }
}