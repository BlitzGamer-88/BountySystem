package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.bounty.Bounty
import com.blitzoffline.bountysystem.bounty.getRandomId
import com.blitzoffline.bountysystem.bounty.minId
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.config.settings
import com.blitzoffline.bountysystem.util.broadcast
import com.blitzoffline.bountysystem.util.createGUI
import com.blitzoffline.bountysystem.util.msg
import com.blitzoffline.bountysystem.util.parsePAPI
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command("bounty")
class CommandBountySystem : CommandBase() {
    @Default
    @Permission("bountysystem.open")
    fun openMenu(sender: Player) {
        if (BOUNTIES_LIST.isEmpty()) {
            messages[Messages.NO_BOUNTIES_FOUND].msg(sender)
            return
        }
        val gui = createGUI()
        gui.open(sender)
    }


    @SubCommand("place")
    @Permission("bountysystem.place")
    fun placeBounty(sender: Player, @Completion("#players") target: Player, @Completion("#amount") amount: String) {
        if (amount.toIntOrNull() == null) {
            messages[Messages.WRONG_USAGE].msg(sender)
            return
        }

        if (target == sender) {
            messages[Messages.BOUNTY_ON_YOURSELF].msg(sender)
            return
        }

        if (econ.getBalance(sender) < amount.toDouble()) {
            messages[Messages.NOT_ENOUGH_MONEY].msg(sender)
            return
        }

        if (target.hasPermission("bountysystem.bypass")) {
            messages[Messages.TARGET_WHITELISTED].msg(sender)
            return
        }

        var bountiesCounter = 0
        for (bounty in BOUNTIES_LIST.values) {
            if (bounty.id < minId) continue
            if (sender.uniqueId.toString() == bounty.payer) bountiesCounter++
        }

        if (bountiesCounter >= settings[Bounties.MAX_AMOUNT]) {
            messages[Messages.MAX_BOUNTIES].msg(sender)
            return
        }

        val bountyId = getRandomId()
        if (bountyId == 0.toShort()) return

        econ.withdrawPlayer(sender, amount.toDouble())
        BOUNTIES_LIST[bountyId.toString()] = Bounty(
            bountyId,
            sender.uniqueId.toString(),
            target.uniqueId.toString(),
            amount.toInt(),
            System.currentTimeMillis()/1000
        )

        val finalAmount = amount.toInt() - ((settings[Bounties.TAX] / 100) * amount.toInt())
        messages[Messages.BOUNTY_PLACED_SELF]
            .replace("%target%", target.name)
            .replace("%amount%", finalAmount.toString())
            .replace("%bountyId%", bountyId.toString())
            .msg(sender)

        messages[Messages.BOUNTY_PLACED_EVERYONE]
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
            messages[Messages.WRONG_USAGE].msg(sender)
            return
        }

        if (!BOUNTIES_LIST.keys.contains(bountyId)) {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyId%", bountyId).msg(sender)
            return
        }

        val bounty = BOUNTIES_LIST[bountyId] ?: return
        if (sender.uniqueId.toString() != bounty.payer) {
            messages[Messages.NOT_YOUR_BOUNTY].replace("%bountyId%", bountyId).msg(sender)
            return
        }

        val newAmount = bounty.amount + amount.toInt()
        econ.withdrawPlayer(sender, amount.toDouble())

        val savedAmount = bounty.amount

        bounty.amount = newAmount
        BOUNTIES_LIST[bountyId] = bounty

        val finalAmount = newAmount - ((settings[Bounties.TAX] / 100) * newAmount)
        messages[Messages.AMOUNT_UPDATED].replace("%newAmount%", finalAmount.toString()).replace("%oldAmount%", savedAmount.toString()).msg(sender)
    }

    @SubCommand("cancel")
    @Permission("bountysystem.cancel")
    fun bountyCancelCommand(sender: Player, @Completion("#id") bountyId: String) {
        if (bountyId.toIntOrNull() == null) {
            messages[Messages.WRONG_USAGE].msg(sender)
            return
        }

        if (!BOUNTIES_LIST.keys.contains(bountyId)) {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyId%", bountyId).msg(sender)
            return
        }

        val bounty = BOUNTIES_LIST[bountyId] ?: return
        if (sender.uniqueId.toString() != bounty.payer) {
            messages[Messages.NOT_YOUR_BOUNTY].replace("%bountyId%", bountyId).msg(sender)
            return
        }

        econ.depositPlayer(sender, bounty.amount.toDouble())
        BOUNTIES_LIST.remove(bountyId)
        messages[Messages.BOUNTY_CANCELED].replace("%bountyId%", bountyId).msg(sender)
    }

    @SubCommand("help")
    fun help(sender: CommandSender) {
        "&7/bounty place <player> <amount> &8- &fPlace a bounty on a player's head".msg(sender)
        "&7/bounty add <bountyID> <amount> &8- &fIncrease a bounty amount.".msg(sender)
        "&7/bounty cancel <bountyID> &8- &fCancel a bounty".msg(sender)
    }

}
