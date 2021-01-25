package me.blitzgamer_88.bountysystem.commands

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.bounty.Bounty
import me.blitzgamer_88.bountysystem.util.*
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


@Command("bounty")
class CommandBountySystem(private val plugin: BountySystem) : CommandBase() {

    @Default
    @Permission("bountysystem.open")
    fun openMenu(sender: Player) {
        if (plugin.BOUNTIES_LIST.isEmpty()) {
            noBountiesFound.msg(sender)
            return
        }
        createGUI(plugin)
        bountyGui.open(sender)
    }


    @SubCommand("place")
    @Permission("bountysystem.place")
    fun placeBounty(sender: Player, @Completion("#players") target: Player, @Completion("#amount") amount: String) {
        if (amount.toIntOrNull() == null) {
            wrongUsage.msg(sender)
            return
        }

        if (target == sender) {
            bountyOnYourself.msg(sender)
            return
        }

        if (econ.getBalance(sender) < amount.toDouble()) {
            notEnoughMoney.msg(sender)
            return
        }

        if (target.hasPermission("bountysystem.bypass")) {
            targetWhitelisted.msg(sender)
            return
        }

        var bountiesCounter = 0
        for (bounty in plugin.BOUNTIES_LIST.values) {
            if (bounty.id < minId || bounty.id > maxId) continue
            if (sender.uniqueId.toString() == bounty.payer) bountiesCounter++
        }

        if (bountiesCounter >= maxBountiesPerPlayer) {
            maxBounties.msg(sender)
            return
        }

        val bountyId = getRandomId(plugin)
        if (bountyId == 0) return

        econ.withdrawPlayer(sender, amount.toDouble())
        plugin.BOUNTIES_LIST[bountyId.toString()] = Bounty(
            bountyId,
            sender.uniqueId.toString(),
            target.uniqueId.toString(),
            amount.toInt(),
            System.currentTimeMillis()/1000
        )

        val finalAmount = amount.toInt() - ((bountyTax / 100) * amount.toInt())
        bountyPlacedSelf
            .replace("%target%", target.name)
            .replace("%amount%", finalAmount.toString())
            .replace("%bountyId%", bountyId.toString())
            .msg(sender)

        bountyPlacedEveryone
            .replace("%target%", target.name)
            .replace("%amount%", finalAmount.toString())
            .replace("%bountyId%", bountyId.toString())
            .parsePAPI(sender)
            .broadcast()
    }


    @SubCommand("add")
    @Permission("bountysystem.add")
    fun bountyAddCommand(sender: Player, @Completion("#id") bountyId: String, @Completion("#amount") amount: String) {
        if (bountyId.toIntOrNull() == null || amount.toIntOrNull() == null) {
            wrongUsage.msg(sender)
            return
        }

        if (!plugin.BOUNTIES_LIST.keys.contains(bountyId)) {
            bountyNotFound.replace("%bountyId%", bountyId).msg(sender)
            return
        }

        val bounty = plugin.BOUNTIES_LIST[bountyId] ?: return
        if (sender.uniqueId.toString() != bounty.payer) {
            notYourBounty.replace("%bountyId%", bountyId).msg(sender)
            return
        }

        val newAmount = bounty.amount + amount.toInt()
        econ.withdrawPlayer(sender, amount.toDouble())

        val savedAmount = bounty.amount

        bounty.amount = newAmount
        plugin.BOUNTIES_LIST[bountyId] = bounty

        val finalAmount = newAmount - ((bountyTax / 100) * newAmount)
        amountUpdated.replace("%newAmount%", finalAmount.toString()).replace("%oldAmount%", savedAmount.toString()).msg(sender)
    }

    @SubCommand("cancel")
    @Permission("bountysystem.cancel")
    fun bountyCancelCommand(sender: Player, @Completion("#id") bountyId: String) {
        if (bountyId.toIntOrNull() == null) {
            wrongUsage.msg(sender)
            return
        }

        if (!plugin.BOUNTIES_LIST.keys.contains(bountyId)) {
            bountyNotFound.replace("%bountyId%", bountyId).msg(sender)
            return
        }

        val bounty = plugin.BOUNTIES_LIST[bountyId] ?: return
        if (sender.uniqueId.toString() != bounty.payer) {
            notYourBounty.replace("%bountyId%", bountyId).msg(sender)
            return
        }

        econ.depositPlayer(sender, bounty.amount.toDouble())
        plugin.BOUNTIES_LIST.remove(bountyId)
        bountyCanceled.replace("%bountyId%", bountyId).msg(sender)
    }

    @SubCommand("help")
    fun help(sender: CommandSender) {
        "&7/bounty place <player> <amount> &8- &fPlace a bounty on a player's head".msg(sender)
        "&7/bounty add <bountyID> <amount> &8- &fIncrease a bounty amount.".msg(sender)
        "&7/bounty cancel <bountyID> &8- &fCancel a bounty".msg(sender)
    }

}
