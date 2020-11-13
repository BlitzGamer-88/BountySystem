package me.blitzgamer_88.bountysystem.cmd

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.conf.Config
import me.blitzgamer_88.bountysystem.util.chat.broadcast
import me.blitzgamer_88.bountysystem.util.chat.msg
import me.blitzgamer_88.bountysystem.util.chat.parsePAPI
import me.blitzgamer_88.bountysystem.util.conf.conf
import me.blitzgamer_88.bountysystem.util.conf.econ
import me.blitzgamer_88.bountysystem.util.conf.maxId
import me.blitzgamer_88.bountysystem.util.conf.minId
import me.blitzgamer_88.bountysystem.util.gui.Bounty
import me.blitzgamer_88.bountysystem.util.gui.bountyGui
import me.blitzgamer_88.bountysystem.util.gui.updateGui
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Completion
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Bukkit.getServer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*


@Command("bounty")
class CommandBountySystem(private val plugin: BountySystem) : CommandBase() {

    @Default
    fun mainCommand(sender: Player) {

        // Perms
        val bountyOpenPermission = conf().getProperty(Config.bountyOpenPermission)
        // Messages
        val noPermission = conf().getProperty(Config.noPermission)
        val noBountiesFound = conf().getProperty(Config.noBountiesFound)
        // Others
        val ids = plugin.BOUNTIES_LIST.keys

        if (!sender.hasPermission(bountyOpenPermission)) {
            noPermission.msg(sender)
            return
        }

        if (ids.size < 1) {
            noBountiesFound.msg(sender)
            return
        }
        bountyGui?.open(sender)
    }


    @SubCommand("place")
    fun bountyPlaceCommand(sender: Player, @Completion("#players") targetName: String, @Completion("#amount") amt: Int?) {

        // Perms
        val bountyPlacePermission = conf().getProperty(Config.bountyPlacePermission)
        val bountyByPassPermission = conf().getProperty(Config.bountyByPassPermission)
        // Messages
        val noPermission = conf().getProperty(Config.noPermission)
        val wrongUsage = conf().getProperty(Config.wrongUsage)
        val playerNotFound = conf().getProperty(Config.playerNotFound)
        val targetHasBounty = conf().getProperty(Config.targetHasBounty)
        val notEnoughMoney = conf().getProperty(Config.notEnoughMoney)
        val targetWhitelisted = conf().getProperty(Config.targetWhitelisted)
        val bountyPlacedSelf = conf().getProperty(Config.bountyPlacedSelf)
        val maxBounties = conf().getProperty(Config.maxBounties)
        val bountyOnYourself = conf().getProperty(Config.bountyOnYourself)
        val bountyPlacedEveryone = conf().getProperty(Config.bountyPlacedEveryone).parsePAPI(sender)
        // Others
        val maxBountiesPerPlayer = conf().getProperty(Config.maxBountiesPerPlayer)
        val bountyTax = conf().getProperty(Config.bountyTax)

        if (!sender.hasPermission(bountyPlacePermission)) {
            noPermission.msg(sender)
            return
        }

        val amount = amt.toString().toIntOrNull()
        if (amount == null) {
            wrongUsage.msg(sender)
            return
        }

        val targetPlayer = getServer().getPlayer(targetName)
        if (targetPlayer == null) {
            playerNotFound.msg(sender)
            return
        }

        if (targetPlayer == sender) {
            bountyOnYourself.msg(sender)
            return
        }

        val balance = econ?.getBalance(sender)
        if (balance == null || balance < amount.toDouble()) {
            notEnoughMoney.msg(sender)
            return
        }

        // Check if the player is whitelisted first
        if (targetPlayer.hasPermission(bountyByPassPermission)) {
            targetWhitelisted.msg(sender)
            return
        }

        val bounties = plugin.BOUNTIES_LIST
        val ids = bounties.keys

        var bountiesCounter = 0
        for (id in ids) {
            val bounty = bounties[id] ?: continue
            val payerUniqueIdString = bounty.payer
            if (sender.uniqueId.toString() == payerUniqueIdString) bountiesCounter++
        }

        if (bountiesCounter >= maxBountiesPerPlayer) {
            maxBounties.msg(sender)
            return
        }

        // CHECK IF THERE IS A BOUNTY ON THAT PLAYER ALREADY AND STOP IF THERE IS
        for (id in ids) {
            val newId = id.toIntOrNull() ?: continue
            if (newId < minId || newId > maxId) continue
            val bounty = bounties[id] ?: continue
            val targetUniqueIdString = bounty.target ?: continue
            val targetUniqueId = UUID.fromString(targetUniqueIdString)
            if (targetUniqueId == targetPlayer.uniqueId) {
                targetHasBounty.msg(sender)
                return
            }
        }

        // IF THERE IS NO BOUNTY ON THAT PLAYER, PLACE ONE. FIRST CREATE A NEW BOUNTY ID
        var bountyId = (minId..maxId).random()
        var idExists = true
        while (idExists) {
            val string = bountyId.toString()
            if (!ids.contains(string)) {
                idExists = false
                break
            }
            bountyId = (minId..maxId).random()
        }

        val newId = bountyId.toString()
        val currentTimeInSeconds = System.currentTimeMillis() / 1000

        // NOW CREATE THE BOUNTY
        econ?.withdrawPlayer(sender, amount.toDouble())
        val newBounty = Bounty()
        newBounty.id = bountyId
        newBounty.target = targetPlayer.uniqueId.toString()
        newBounty.payer = sender.uniqueId.toString()
        newBounty.amount = amount
        newBounty.placedTime = currentTimeInSeconds

        plugin.BOUNTIES_LIST[newId] = newBounty

        val newAmount = amount - ((bountyTax/100)*amount)

        bountyPlacedSelf.replace("%target%", targetName).replace("%amount%", newAmount.toString()).replace("%bountyId%", newId).msg(sender)
        bountyPlacedEveryone.replace("%target%", targetName).replace("%amount%", newAmount.toString()).replace("%bountyId%", newId).broadcast()
        updateGui(plugin)
    }

    @SubCommand("add")
    fun bountyAddCommand(sender: Player, @Completion("#id") bountyId: String, @Completion("#amount") amt: Int?) {

        // Perms
        val bountyAddPermission = conf().getProperty(Config.bountyAddPermission)
        // Messages
        val noPermission = conf().getProperty(Config.noPermission)
        val wrongUsage = conf().getProperty(Config.wrongUsage)
        val bountyNotFound = conf().getProperty(Config.bountyNotFound)
        val notYourBounty = conf().getProperty(Config.notYourBounty)
        val amountUpdated = conf().getProperty(Config.amountUpdated)
        // Others
        val ids = plugin.BOUNTIES_LIST.keys

        if (!sender.hasPermission(bountyAddPermission)) {
            noPermission.msg(sender)
            return
        }

        if (bountyId.toIntOrNull() == null) {
            wrongUsage.msg(sender)
            return
        }

        val amount = amt.toString().toIntOrNull()
        if (amount == null) {
            wrongUsage.msg(sender)
            return
        }

        if (!ids.contains(bountyId)) {
            bountyNotFound.replace("%bountyId%", bountyId).msg(sender)
            return
        }

        // CHECK IF BOUNTY IS PLACED BY SENDER
        val bounties = plugin.BOUNTIES_LIST
        val bounty = bounties[bountyId] ?: return
        val placerUniqueIdString = bounty.payer ?: return
        val senderUUIDString = sender.uniqueId.toString()

        if (placerUniqueIdString != senderUUIDString) {
            notYourBounty.replace("%bountyId%", bountyId).msg(sender)
            return
        }

        val savedAmount = bounty.amount ?: return
        val newAmount = savedAmount+amount
        econ?.withdrawPlayer(sender, amount.toDouble())
        bounty.amount = newAmount
        plugin.BOUNTIES_LIST[bountyId] = bounty
        amountUpdated.replace("%newAmount%", amount.toString()).replace("%oldAmount%", savedAmount.toString()).msg(sender)
        updateGui(plugin)
    }

    @SubCommand("cancel")
    fun bountyCancelCommand(sender: Player, @Completion("#id") bId: Int?) {

        // Perms
        val bountyCancelPermission = conf().getProperty(Config.bountyCancelPermission)
        // Messages
        val noPermission = conf().getProperty(Config.noPermission)
        val wrongUsage = conf().getProperty(Config.wrongUsage)
        val bountyNotFound = conf().getProperty(Config.bountyNotFound)
        val notYourBounty = conf().getProperty(Config.notYourBounty)
        val bountyCanceled = conf().getProperty(Config.bountyCanceled)
        // Others
        val ids = plugin.BOUNTIES_LIST.keys

        if (!sender.hasPermission(bountyCancelPermission)) {
            noPermission.msg(sender)
            return
        }

        val bountyId = bId.toString()
        if (bountyId.toIntOrNull() == null) {
            wrongUsage.msg(sender)
            return
        }

        if (!ids.contains(bountyId)) {
            bountyNotFound.replace("%bountyId%", bountyId).msg(sender)
            return
        }

        // CHECK IF BOUNTY IS PLACED BY SENDER
        val bounties = plugin.BOUNTIES_LIST
        val bounty = bounties[bountyId] ?: return
        val placerUniqueIdString = bounty.payer ?: return
        val senderUUIDString = sender.uniqueId.toString()

        if (placerUniqueIdString != senderUUIDString) {
            notYourBounty.replace("%bountyId%", bountyId).msg(sender)
            return
        }

        // REMOVE BOUNTY
        val amount = bounty.amount ?: return
        econ?.depositPlayer(sender, amount.toDouble())
        plugin.BOUNTIES_LIST.remove(bountyId)
        bountyCanceled.replace("%bountyId%", bountyId).msg(sender)
        updateGui(plugin)
    }

    @SubCommand("help")
    fun help(sender: CommandSender) {

        "&7/bounty place <player> <amount> &8- &fPlace a bounty on a player's head".msg(sender)
        "&7/bounty add <bountyID> <amount> &8- &fAdd more money to a bounty".msg(sender)
        "&7/bounty cancel <bountyID> &8- &fCancel a bounty".msg(sender)

    }

}
