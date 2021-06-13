package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.util.sendMessage
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
    fun cancel(sender: Player, @Completion("#id") bountyId: String) {
        if (bountyId.toShortOrNull() == null) {
            messages[Messages.WRONG_USAGE].sendMessage(sender)
            return
        }

        if (plugin.bountyHandler.bounties.none { it.id == bountyId.toShort() }) {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyId%", bountyId).sendMessage(sender)
            return
        }

        val bounty = plugin.bountyHandler.bounties.firstOrNull { it.id == bountyId.toShort() } ?: run {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyId%", bountyId).sendMessage(sender)
            return
        }

        if (sender.uniqueId != bounty.payer) {
            messages[Messages.NOT_YOUR_BOUNTY].replace("%bountyId%", bountyId).sendMessage(sender)
            return
        }

        plugin.economy.depositPlayer(sender, bounty.amount.toDouble())
        plugin.bountyHandler.bounties.remove(bounty)
        messages[Messages.BOUNTY_CANCELED].replace("%bountyId%", bountyId).sendMessage(sender)
    }
}