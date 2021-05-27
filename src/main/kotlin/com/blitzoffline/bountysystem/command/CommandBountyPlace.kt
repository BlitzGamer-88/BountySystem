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
import com.blitzoffline.bountysystem.util.debug
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

        "&e[/bounty place] The command was initialised.".debug()
        if (amount.toIntOrNull() == null) {
            messages[Messages.WRONG_USAGE].msg(sender)
            "&e[/bounty place] The command only accepts an integer as amount. PLACED: $amount".debug()
            return
        }

        if (target == sender) {
            messages[Messages.BOUNTY_ON_YOURSELF].msg(sender)
            "&e[/bounty place] Placing a bounty on yourself is not allowed.".debug()
            return
        }

        if (econ.getBalance(sender) < amount.toDouble()) {
            messages[Messages.NOT_ENOUGH_MONEY].msg(sender)
            "&e[/bounty place] Not enough money. GOT: ${econ.getBalance(sender)}, PLACED: ${amount.toInt()}".debug()
            return
        }

        if (target.hasPermission("bountysystem.bypass")) {
            messages[Messages.TARGET_WHITELISTED].msg(sender)
            "&e[/bounty place] The target: ${target.uniqueId} has the bypass permission".debug()
            return
        }

        var bountiesCounter = 0
        for (bounty in BOUNTIES_LIST.values) {
            if (bounty.id < minId) continue
            if (sender.uniqueId == bounty.payer) bountiesCounter++
        }

        if (bountiesCounter >= settings[Bounties.MAX_AMOUNT]) {
            messages[Messages.MAX_BOUNTIES].msg(sender)
            "&e[/bounty place] You have placed: $bountiesCounter bounties out of: ${settings[Bounties.MAX_AMOUNT]} allowed"
            return
        }

        val bountyId = getRandomId()
        if (bountyId == 0.toShort()) {
            "&e[/bounty place] Could not find an empty ID for your bounty. This usually means that the maximum amount of 999000 bounties was reached.".debug()
            return
        }

        econ.withdrawPlayer(sender, amount.toDouble())
        BOUNTIES_LIST[bountyId.toString()] = Bounty(
            bountyId,
            sender.uniqueId,
            target.uniqueId,
            amount.toInt(),
            System.currentTimeMillis()
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
        "&e[/bounty place] Bounty was successfully placed.".debug()
    }

}