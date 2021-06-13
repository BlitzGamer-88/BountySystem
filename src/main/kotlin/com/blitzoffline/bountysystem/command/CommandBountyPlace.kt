package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.bounty.Bounty
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.util.broadcastMessage
import com.blitzoffline.bountysystem.util.sendMessage
import com.blitzoffline.bountysystem.util.parsePAPI
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Completion
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command("bounty")
class CommandBountyPlace(private val plugin: BountySystem) : CommandBase() {
    private val messages = plugin.messages
    private val settings = plugin.settings

    @SubCommand("place")
    @Permission("bountysystem.place")
    fun place(sender: Player, @Completion("#players") target: Player, @Completion("#amount") amount: String) {

        if (amount.toIntOrNull() == null) {
            messages[Messages.WRONG_USAGE].sendMessage(sender)
            return
        }

        if (target == sender) {
            messages[Messages.BOUNTY_ON_YOURSELF].sendMessage(sender)
            return
        }

        if (target.hasPermission("bountysystem.bypass")) {
            messages[Messages.TARGET_WHITELISTED].sendMessage(sender)
            return
        }

        if (plugin.economy.getBalance(sender) < amount.toDouble()) {
            messages[Messages.NOT_ENOUGH_MONEY].sendMessage(sender)
            return
        }

        if (plugin.bountyHandler.bounties.filter { it.payer == sender.uniqueId }.size >= settings[Bounties.MAX_AMOUNT]) {
            messages[Messages.MAX_BOUNTIES].sendMessage(sender)
            return
        }

        val bountyId = plugin.bountyHandler.getRandomId()
        if (bountyId == 0.toShort()) {
            return
        }

        plugin.economy.withdrawPlayer(sender, amount.toDouble())
        val bounty = Bounty(
            bountyId,
            sender.uniqueId,
            target.uniqueId,
            amount.toInt(),
            System.currentTimeMillis()
        )
        plugin.bountyHandler.bounties.add(bounty)

        val afterTax = bounty.amount - ((settings[Bounties.TAX] / 100) * bounty.amount)

        messages[Messages.BOUNTY_PLACED_SELF]
            .replace("%target%", target.name)
            .replace("%amount%", afterTax.toString())
            .replace("%bountyId%", bountyId.toString())
            .sendMessage(sender)

        messages[Messages.BOUNTY_PLACED_EVERYONE]
            .replace("%target%", target.name)
            .replace("%amount%", afterTax.toString())
            .replace("%bountyId%", bountyId.toString())
            .parsePAPI(sender)
            .broadcastMessage()
    }
}