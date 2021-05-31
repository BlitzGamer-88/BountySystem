package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.bounty.Bounty
import com.blitzoffline.bountysystem.bounty.getRandomId
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.config.settings
import com.blitzoffline.bountysystem.util.broadcast
import com.blitzoffline.bountysystem.util.msg
import com.blitzoffline.bountysystem.util.parsePAPI
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Completion
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command("bounty")
class CommandBountyPlace : CommandBase() {

    @SubCommand("place")
    @Permission("bountysystem.place")
    fun place(sender: Player, @Completion("#players") target: Player, @Completion("#amount") amount: String) {

        if (amount.toIntOrNull() == null) {
            messages[Messages.WRONG_USAGE].msg(sender)
            return
        }

        if (target == sender) {
            messages[Messages.BOUNTY_ON_YOURSELF].msg(sender)
            return
        }

        if (target.hasPermission("bountysystem.bypass")) {
            messages[Messages.TARGET_WHITELISTED].msg(sender)
            return
        }

        if (econ.getBalance(sender) < amount.toDouble()) {
            messages[Messages.NOT_ENOUGH_MONEY].msg(sender)
            return
        }

        if (BOUNTIES_LIST.filter { it.payer == sender.uniqueId }.size >= settings[Bounties.MAX_AMOUNT]) {
            messages[Messages.MAX_BOUNTIES].msg(sender)
            return
        }

        val bountyID = getRandomId()
        if (bountyID == 0.toShort()) {
            return
        }

        econ.withdrawPlayer(sender, amount.toDouble())
        val bounty = Bounty(
            bountyID,
            sender.uniqueId,
            target.uniqueId,
            amount.toInt(),
            System.currentTimeMillis()
        )
        BOUNTIES_LIST.add(bounty)

        messages[Messages.BOUNTY_PLACED_SELF]
            .replace("%target%", target.name)
            .replace("%amount%", bounty.afterTax().toString())
            .replace("%bountyId%", bountyID.toString())
            .msg(sender)

        messages[Messages.BOUNTY_PLACED_EVERYONE]
            .replace("%target%", target.name)
            .replace("%amount%", bounty.afterTax().toString())
            .replace("%bountyId%", bountyID.toString())
            .parsePAPI(sender)
            .broadcast()
    }

}