package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.util.msg
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Completion
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command("bounty")
class CommandBountyCancel : CommandBase() {

    @SubCommand("cancel")
    @Permission("bountysystem.cancel")
    fun cancel(sender: Player, @Completion("#id") bountyId: String) {
        if (bountyId.toIntOrNull() == null) {
            messages[Messages.WRONG_USAGE].msg(sender)
            return
        }

        if (!BOUNTIES_LIST.keys.contains(bountyId)) {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyId%", bountyId).msg(sender)
            return
        }

        val bounty = BOUNTIES_LIST[bountyId] ?: run {
            messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyId%", bountyId).msg(sender)
            return
        }

        if (sender.uniqueId != bounty.payer) {
            messages[Messages.NOT_YOUR_BOUNTY].replace("%bountyId%", bountyId).msg(sender)
            return
        }

        econ.depositPlayer(sender, bounty.amount.toDouble())
        BOUNTIES_LIST.remove(bountyId)
        messages[Messages.BOUNTY_CANCELED].replace("%bountyId%", bountyId).msg(sender)
    }

}