package me.blitzgamer_88.bountysystem.cmd

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.conf.Config
import me.blitzgamer_88.bountysystem.runnable.GetCache
import me.blitzgamer_88.bountysystem.runnable.SaveCache
import me.blitzgamer_88.bountysystem.util.chat.color
import me.blitzgamer_88.bountysystem.util.chat.msg
import me.blitzgamer_88.bountysystem.util.chat.parsePAPI
import me.blitzgamer_88.bountysystem.util.conf.conf
import me.blitzgamer_88.bountysystem.util.conf.econ
import me.blitzgamer_88.bountysystem.util.conf.perms
import me.blitzgamer_88.bountysystem.util.gui.loadDefaultGui
import me.blitzgamer_88.bountysystem.util.gui.updateGui
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@Command("bountyadmin")
@Alias("badmin")
class CommandBountySystemAdmin(private val plugin: BountySystem) : CommandBase() {

    @SubCommand("cancel")
    fun adminCancel(sender: CommandSender, bountyId: String) {

        // Perms
        val adminPermission = conf().getProperty(Config.adminPermission)
        val adminBountyCancelPermission = conf().getProperty(Config.adminBountyCancelPermission)
        // Messages
        val wrongUsage = conf().getProperty(Config.wrongUsage).color()
        val bountyNotFound = conf().getProperty(Config.bountyNotFound).color()
        val bountyCanceled = conf().getProperty(Config.bountyCanceled).color()
        val bountyCanceledByAdmin = conf().getProperty(Config.bountyCanceledByAdmin).color()
        val noPermission = conf().getProperty(Config.noPermission)
        // Others
        val bountyTax = conf().getProperty(Config.bountyTax)
        val ids = plugin.BOUNTIES_LIST.keys

        if (sender is Player && !sender.hasPermission(adminBountyCancelPermission) && !sender.hasPermission(adminPermission)) {
            noPermission.msg(sender)
            return
        }

        if (bountyId.toIntOrNull() == null) {
            sender.sendMessage(wrongUsage)
            return
        }

        if (!ids.contains(bountyId)) {
            sender.sendMessage(bountyNotFound.replace("%bountyID%", bountyId))
            return
        }

        val bounties = plugin.BOUNTIES_LIST
        val bounty = bounties[bountyId] ?: return

        // CANCEL BOUNTY WITH BOUNTY-ID
        val amount = bounty.amount ?: return
        val payerUniqueIdString = bounty.payer ?: return
        val payerUniqueId = UUID.fromString(payerUniqueIdString)
        val payerOfflinePlayer = Bukkit.getOfflinePlayer(payerUniqueId)
        val payer = payerOfflinePlayer.player

        econ?.depositPlayer(payerOfflinePlayer, amount.toDouble())
        plugin.BOUNTIES_LIST.remove(bountyId)
        val newAmount = amount - ((bountyTax/100)*amount)
        updateGui(plugin)
        bountyCanceled.replace("%amount%", newAmount.toString()).replace("%bountyId%", bountyId).msg(sender)
        if (payer == null) return
        bountyCanceledByAdmin.replace("%amount%", newAmount.toString()).replace("%bountyId%", bountyId).msg(payer)
    }

    @SubCommand("bypass")
    fun adminBypass(sender: CommandSender, @Completion("#players") playerName: String) {

        // Permissions
        val adminPermission = conf().getProperty(Config.adminPermission)
        val adminBypassPermission = conf().getProperty(Config.adminBypassPermission)
        val bountyByPassPermission = conf().getProperty(Config.bountyByPassPermission)
        // Messages
        val wrongUsage = conf().getProperty(Config.wrongUsage).color()
        val playerWhitelisted = conf().getProperty(Config.playerWhitelisted).color()
        val noPermission = conf().getProperty(Config.noPermission)

        if (sender is Player && !sender.hasPermission(adminBypassPermission) && !sender.hasPermission(adminPermission)) {
            noPermission.msg(sender)
            return
        }

        val player = Bukkit.getPlayer(playerName)
        if (player == null) {
            sender.sendMessage(wrongUsage)
            return
        }

        perms?.playerAdd(player, bountyByPassPermission)
        sender.sendMessage(playerWhitelisted.parsePAPI(player))
    }

    @SubCommand("reload")
    fun adminReload(sender: CommandSender) {

        // Permissions
        val adminPermission = conf().getProperty(Config.adminPermission)
        val adminReloadPermission = conf().getProperty(Config.adminReloadPermission)
        // Messages
        val noPermission = conf().getProperty(Config.noPermission)
        val configReloaded = conf().getProperty(Config.configReloaded).color()

        if (sender is Player && !sender.hasPermission(adminReloadPermission) && !sender.hasPermission(adminPermission)) {
            noPermission.msg(sender)
            return
        }

        conf().reload()
        SaveCache(plugin).runTask(plugin)
        GetCache(plugin).runTask(plugin)
        updateGui(plugin)
        configReloaded.msg(sender)
    }
}
