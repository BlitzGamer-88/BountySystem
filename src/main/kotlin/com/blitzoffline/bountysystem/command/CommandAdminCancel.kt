package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages
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
class CommandAdminCancel : CommandBase() {

    @SubCommand("cancel")
    @Permission("bountysystem.admin")
    fun cancel(sender: CommandSender, @Completion("#id") bountyId: String) {
        if (!BOUNTIES_LIST.keys.contains(bountyId)) {
            sender.sendMessage(messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyID%", bountyId))
            return
        }

        val bounty = BOUNTIES_LIST[bountyId] ?: run {
            messages[Messages.BOUNTY_NOT_FOUND].msg(sender)
            return
        }

        econ.depositPlayer(bounty.payer(), bounty.amount.toDouble())
        BOUNTIES_LIST.remove(bountyId)

        messages[Messages.BOUNTY_CANCELED]
            .replace("%bountyId%", bountyId)
            .msg(sender)

        bounty.payer().player?.let {
            messages[Messages.BOUNTY_CANCELED_ADMIN]
                .replace("%bountyId%", bountyId)
                .msg(it)
        }
    }

}