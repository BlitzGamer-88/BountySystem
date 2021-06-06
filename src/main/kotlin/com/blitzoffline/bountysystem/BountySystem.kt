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
import com.blitzoffline.bountysystem.util.log
import com.blitzoffline.bountysystem.util.msg
import me.mattstudios.config.SettingsManager
import me.mattstudios.mf.base.CommandManager
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
    lateinit var database: Database

    lateinit var settings: SettingsManager
    lateinit var messages: SettingsManager
    lateinit var economy: Economy

    override fun onEnable() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig")
        } catch (ignored: ClassNotFoundException) {
            "&cTHIS PLUGIN SHOULD BE USED ON PAPER: papermc.io/download".log()
        }

        configHandler = ConfigHandler(this)
        settings = configHandler.loadConfig()
        messages = configHandler.loadMessages()
        economy = configHandler.loadEconomy() ?: run {
            "[BountySystem] Could not find Vault! This plugin is required".log()
            pluginLoader.disablePlugin(this)
            return
        }

        bountyHandler = BountyHandler(this)
        database = Database(this)

        if (!setupHooks()) pluginLoader.disablePlugin(this)
        BountyPlaceholders(this).register()
        registerListeners(
            PlayerDeathListener(this)
        )

        commandManager = CommandManager(this, true)
        with (commandManager.messageHandler) {
            register("cmd.wrong.usage") { sender -> messages[Messages.WRONG_USAGE].msg(sender) }
            register("cmd.no.permission") { sender -> messages[Messages.NO_PERMISSION].msg(sender) }
        }
        with (commandManager.completionHandler) {
            register("#id") { listOf("<bountyId>") }
            register("#amount") { listOf("<amount>") }
        }
        with (commandManager) {
            register(
                CommandBounty(this@BountySystem),
                CommandBountyIncrease(this@BountySystem),
                CommandBountyCancel(this@BountySystem),
                CommandBountyHelp(),
                CommandBountyPlace(this@BountySystem),
                CommandAdminCancel(this@BountySystem),
                CommandAdminReload(this@BountySystem)
            )
        }

        registerTasks()
        database.load()
        "[BountySystem] Plugin enabled successfully!".log()
    }

    override fun onDisable() {
        database.save()
        "[BountySystem] Plugin disabled successfully!".log()
    }

    private fun registerTasks() {
        if(::saveData.isInitialized && !saveData.isCancelled) saveData.cancel()
        if(::updateBounties.isInitialized && !updateBounties.isCancelled) updateBounties.cancel()

        saveData = SaveDataTask(this).runTaskTimer(this, settings[Settings.INTERVAL_CACHE] * 20L, settings[Settings.INTERVAL_CACHE] * 20L)
        updateBounties = BountyExpire(this).runTaskTimerAsynchronously(this, settings[Settings.INTERVAL_EXPIRY] * 20L, settings[Settings.INTERVAL_EXPIRY] * 20L)
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
        return true
    }

    private fun registerListeners(vararg listeners: Listener) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

    fun saveDefaultMessages() {
        if (dataFolder.resolve("messages.yml").exists()) return
        saveResource("messages.yml", false)
    }
}