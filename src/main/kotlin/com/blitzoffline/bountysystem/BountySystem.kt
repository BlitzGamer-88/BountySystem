package com.blitzoffline.bountysystem

import com.google.gson.Gson
import com.blitzoffline.bountysystem.bounty.Bounty
import com.blitzoffline.bountysystem.database.Database
import com.blitzoffline.bountysystem.command.CommandBountySystem
import com.blitzoffline.bountysystem.command.CommandBountySystemAdmin
import com.blitzoffline.bountysystem.listener.PlayerDeathListener
import com.blitzoffline.bountysystem.runnable.BountyExpire
import com.blitzoffline.bountysystem.runnable.SaveCache
import com.blitzoffline.bountysystem.util.*
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mf.base.CommandManager
import me.mattstudios.mf.base.components.CompletionResolver
import me.mattstudios.mf.base.components.MessageResolver
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.io.File


class BountySystem : JavaPlugin() {

    val BOUNTIES_LIST = mutableMapOf<String, Bounty>()
    private lateinit var commandManager: CommandManager
    private lateinit var bountyExpire: BukkitTask
    private lateinit var saveCache: BukkitTask
    lateinit var database: Database
      private set

    override fun onEnable() {

        database = Database(this)

        this.saveDefaultConfig()
        this.saveDefaultMessages()

        loadSettings(this)
        loadMessages(this)

        registerSettings()
        registerMessages()

        if (!setupPAPI()) { "[BountySystem] Could not find PlaceholderAPI! This plugin is required".log() }
        if (!setupEconomy()) { "[BountySystem] Could not find Vault! This plugin is required".log() }
        if (!setupPermissions()) { "[BountySystem] Could not find Vault! This plugin is required".log() }

        registerListeners(PlayerDeathListener(this))

        commandManager = CommandManager(this, true)

        registerMessage("cmd.no.permission") { sender -> noPermission.msg(sender) }
        registerMessage("cmd.wrong.usage") { sender -> wrongUsage.msg(sender) }

        registerCompletion("#id") { listOf("<bountyID>") }
        registerCompletion("#amount") { listOf("<amount>") }
        registerCommands(CommandBountySystem(this), CommandBountySystemAdmin(this))

        database.load()
        createGUI(this)

        bountyExpire = BountyExpire(this).runTaskTimer(this, expiryCheckInterval * 20L, expiryCheckInterval * 20L)
        saveCache = SaveCache(this).runTaskTimer(this, cacheSaveInterval * 20L, cacheSaveInterval * 20L)
        "[BountySystem] Plugin enabled successfully!".log()
    }

    override fun onDisable() {
        database.save()
        "[BountySystem] Plugin disabled successfully!".log()
    }

    fun reload() {
        bountyExpire.cancel()
        saveCache.cancel()
        database.save()
        msg.reload()
        conf.reload()
        bountyExpire = BountyExpire(this).runTaskTimer(this, expiryCheckInterval * 20L, expiryCheckInterval * 20L)
        saveCache = SaveCache(this).runTaskTimer(this, cacheSaveInterval * 20L, cacheSaveInterval * 20L)
    }

    private fun registerCommands(vararg commands: CommandBase) = commands.forEach(commandManager::register)
    private fun registerCompletion(completionId: String, resolver: CompletionResolver) = commandManager.completionHandler.register(completionId, resolver)
    private fun registerMessage(messageId: String, resolver: MessageResolver) = commandManager.messageHandler.register(messageId, resolver)
    private fun registerListeners(vararg listeners: Listener) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

    fun saveDefaultMessages() {
        if (File("messages.yml").exists()) return
        saveResource("messages.yml", false)
    }

    companion object {
        val GSON = Gson()
    }
}