package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.util.sendMessage
import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Completion
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender

@Alias("badmin")
@Command("bountyadmin")
class CommandAdminCancel(private val plugin: BountySystem) : CommandBase() {
    private val messages = plugin.messages

    @SubCommand("cancel")
    @Permission("bountysystem.admin")
    fun cancel(sender: CommandSender, @Completion("#id") bountyId: String) {

        if (bountyId.toShortOrNull() == null) {
            messages[Messages.WRONG_USAGE].sendMessage(sender)
            return
        }

        if (plugin.bountyHandler.bounties.none { it.id == bountyId.toShort() }) {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyId%", bountyId).sendMessage(sender)
            return
        }

        val bounty = plugin.bountyHandler.bounties.firstOrNull { it.id == bountyId.toShort() } ?: run {
            messages[Messages.BOUNTY_NOT_FOUND].sendMessage(sender)
            return
        }

        plugin.economy.depositPlayer(bounty.payer(), bounty.amount.toDouble())
        plugin.bountyHandler.bounties.remove(bounty)

        messages[Messages.BOUNTY_CANCELED]
            .replace("%bountyId%", bountyId)
            .sendMessage(sender)

        bounty.payer().player?.let {
            messages[Messages.BOUNTY_CANCELED_ADMIN]
                .replace("%bountyId%", bountyId)
                .sendMessage(it)
        }
    }
}