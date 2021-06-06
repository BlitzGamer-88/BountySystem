package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.util.msg
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Completion
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command("bounty")
class CommandBountyIncrease(private val plugin: BountySystem) : CommandBase() {
    private val messages = plugin.messages
    private val settings = plugin.settings

    @SubCommand("increase")
    @Permission("bountysystem.increase")
    fun increase(sender: Player, @Completion("#id") bountyID: String, @Completion("#amount") amount: String) {
        if (bountyID.toShortOrNull() == null || amount.toIntOrNull() == null) {
            messages[Messages.WRONG_USAGE].msg(sender)
            return
        }

        if (plugin.bountyHandler.BOUNTIES.none { it.id == bountyID.toShort() }) {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyID%", bountyID).msg(sender)
            return
        }

        val bounty = plugin.bountyHandler.BOUNTIES.firstOrNull { it.id == bountyID.toShort() } ?: run {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyID%", bountyID).msg(sender)
            return
        }

        if (sender.uniqueId != bounty.payer) {
            messages[Messages.NOT_YOUR_BOUNTY].replace("%bountyID%", bountyID).msg(sender)
            return
        }

        val newAmount = bounty.amount + amount.toInt()
        plugin.economy.withdrawPlayer(sender, amount.toDouble())

        val savedAmount = bounty.amount

        bounty.amount = newAmount
        plugin.bountyHandler.BOUNTIES[plugin.bountyHandler.BOUNTIES.indexOf(bounty)] = bounty

        val finalAmount = newAmount - ((settings[Bounties.TAX] / 100) * newAmount)
        messages[Messages.AMOUNT_UPDATED].replace("%newAmount%", finalAmount.toString()).replace("%oldAmount%", savedAmount.toString()).msg(sender)
    }

}