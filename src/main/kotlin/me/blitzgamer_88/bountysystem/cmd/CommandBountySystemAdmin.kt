package me.blitzgamer_88.bountysystem.cmd

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.conf.Config
import me.blitzgamer_88.bountysystem.util.*
import me.clip.placeholderapi.PlaceholderAPI
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
        val ids = plugin.getBounties().getKeys(false)
        val bountyTax = conf().getProperty(Config.bountyTax)

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

        val bounties = plugin.getBounties()

        // CANCEL BOUNTY WITH BOUNTY-ID
        val amount = bounties.getInt("$bountyId.amount")
        val payerUniqueIdString = bounties.getString("$bountyId.placer") ?: return
        val payerUniqueId = UUID.fromString(payerUniqueIdString)
        val payerOfflinePlayer = Bukkit.getOfflinePlayer(payerUniqueId)

        econ?.depositPlayer(payerOfflinePlayer, amount.toDouble())
        bounties.set(bountyId, null)
        plugin.saveBounties()
        val newAmount = amount - ((bountyTax/100)*amount)
        sender.sendMessage(bountyCanceled.replace("%amount%", newAmount.toString()).replace("%bountyId%", bountyId))
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
        plugin.reloadBounties()
        sender.sendMessage(configReloaded)
    }
}