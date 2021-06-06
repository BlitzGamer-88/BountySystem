package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.util.msg
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Completion
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command("bounty")
class CommandBountyCancel(private val plugin: BountySystem) : CommandBase() {
    private val messages = plugin.messages

    @SubCommand("cancel")
    @Permission("bountysystem.cancel")
    fun cancel(sender: Player, @Completion("#id") bountyID: String) {
        if (bountyID.toShortOrNull() == null) {
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

        plugin.economy.depositPlayer(sender, bounty.amount.toDouble())
        plugin.bountyHandler.BOUNTIES.remove(bounty)
        messages[Messages.BOUNTY_CANCELED].replace("%bountyID%", bountyID).msg(sender)
    }
}