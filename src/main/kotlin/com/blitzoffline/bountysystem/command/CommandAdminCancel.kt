package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.util.msg
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
    fun cancel(sender: CommandSender, @Completion("#id") bountyID: String) {

        if (bountyID.toShortOrNull() == null) {
            messages[Messages.WRONG_USAGE].msg(sender)
            return
        }

        if (plugin.bountyHandler.BOUNTIES.none { it.id == bountyID.toShort() }) {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyID%", bountyID).msg(sender)
            return
        }

        val bounty = plugin.bountyHandler.BOUNTIES.firstOrNull { it.id == bountyID.toShort() } ?: run {
            messages[Messages.BOUNTY_NOT_FOUND].msg(sender)
            return
        }

        plugin.economy.depositPlayer(bounty.payer(), bounty.amount.toDouble())
        plugin.bountyHandler.BOUNTIES.remove(bounty)

        messages[Messages.BOUNTY_CANCELED]
            .replace("%bountyId%", bountyID)
            .msg(sender)

        bounty.payer().player?.let {
            messages[Messages.BOUNTY_CANCELED_ADMIN]
                .replace("%bountyId%", bountyID)
                .msg(it)
        }
    }
}