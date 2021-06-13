package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.util.sendMessage
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender

@Command("bounty")
class CommandBountyHelp : CommandBase() {

    @SubCommand("help")
    @Permission("bountysystem.help")
    fun help(sender: CommandSender) {
        if (sender.hasPermission("bountysystem.admin")) {
            "&7/bountyadmin cancel <bountyId> &8- &fForce cancel a bounty. The money is returned to the payer.".sendMessage(sender)
            "&7/bountyadmin reload &8- &fReloads the configuration.".sendMessage(sender)
        }

        "&7/bounty place <player> <amount> &8- &fPlace a bounty on a player's head.".sendMessage(sender)
        "&7/bounty increase <bountyId> <amount> &8- &fIncrease a bounty amount.".sendMessage(sender)
        "&7/bounty cancel <bountyId> &8- &fCancel a bounty you placed.".sendMessage(sender)
    }
}