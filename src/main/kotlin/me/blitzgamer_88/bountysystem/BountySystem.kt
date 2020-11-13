package me.blitzgamer_88.bountysystem

import me.blitzgamer_88.bountysystem.cmd.CommandBountySystem
import me.blitzgamer_88.bountysystem.cmd.CommandBountySystemAdmin
import me.blitzgamer_88.bountysystem.conf.Config
import me.blitzgamer_88.bountysystem.listener.PlayerDeathListener
import me.blitzgamer_88.bountysystem.runnable.BountyExpire
import me.blitzgamer_88.bountysystem.runnable.BountyUpdate
import me.blitzgamer_88.bountysystem.runnable.GetCache
import me.blitzgamer_88.bountysystem.runnable.SaveCache
import me.blitzgamer_88.bountysystem.util.chat.log
import me.blitzgamer_88.bountysystem.util.conf.conf
import me.blitzgamer_88.bountysystem.util.conf.loadConfig
import me.blitzgamer_88.bountysystem.util.conf.setupEconomy
import me.blitzgamer_88.bountysystem.util.gui.Bounty
import me.blitzgamer_88.bountysystem.util.gui.loadDefaultGui
import me.bristermitten.pdm.PDMBuilder
import me.mattstudios.mf.base.CommandManager
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.logging.Level

class BountySystem : JavaPlugin() {

    var BOUNTIES_LIST = HashMap<String, Bounty>()

    override fun onEnable() {
        PDMBuilder(this).build().loadAllDependencies().join()

        this.saveDefaultConfig()

        loadConfig(this)

        GetCache(this).runTask(this)
        loadDefaultGui(this)

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) { "Could not find PlaceholderAPI! This plugin is required".log() }
        if (!setupEconomy()) { "Could not find Vault! This plugin is required".log() }

        server.pluginManager.registerEvents(PlayerDeathListener(this), this)

        val cmdManager = CommandManager(this, true)
        cmdManager.completionHandler.register("#amount") { listOf("<amount>") }
        cmdManager.completionHandler.register("#id") { listOf("<bountyID>") }
        cmdManager.register(CommandBountySystem(this))
        cmdManager.register(CommandBountySystemAdmin(this))

        val runnableCoolDown = conf().getProperty(Config.expiryTimeCheck)
        BountyExpire(this).runTaskTimer(this, 60 * 20L, runnableCoolDown * 60 * 20L)
        BountyUpdate(this).runTaskTimer(this, 200, 200)
        SaveCache(this).runTaskTimer(this, 6000, 6000)

        "[BountySystem] &7Plugin Enabled!".log()
    }

    override fun onDisable() {
        SaveCache(this).runTask(this)
        "[BountySystem] &7Plugin Disabled!".log()
    }


    // BOUNTIES FILE

    private var bountyConfig: FileConfiguration? = null
    private var bountiesFile: File? = null

    fun reloadBounties() {
        if (bountiesFile == null) bountiesFile = File(dataFolder, "bounties.yml")
        bountyConfig = YamlConfiguration.loadConfiguration(bountiesFile!!)
    }

    fun saveBounties() {
        if (bountyConfig == null || bountiesFile == null) return
        try { getBounties().save(bountiesFile!!) }
        catch (ex: IOException) { Bukkit.getServer().logger.log(Level.SEVERE, "Could not save config to $bountiesFile", ex) }
    }

    fun getBounties(): FileConfiguration {
        if (bountyConfig == null) reloadBounties()
        return checkNotNull(bountyConfig)
    }

}