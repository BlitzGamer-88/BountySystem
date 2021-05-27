package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.util.msg
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender

@Command("bounty")
class CommandBountyHelp : CommandBase() {

    @SubCommand("help")
    fun help(sender: CommandSender) {
        if (sender.hasPermission("bountysystem.admin")) {
            "&7/bountyadmin cancel <bountyID> &8- &fForce cancel a bounty. The money is returned to the payer.".msg(sender)
            "&7/bountyadmin bypass <player> &8- &fGives a player the bypass permission".msg(sender)
            "&7/bountyadmin reload &8- &fReloads the configuration".msg(sender)
        }

        "&7/bounty place <player> <amount> &8- &fPlace a bounty on a player's head".msg(sender)
        "&7/bounty increase <bountyID> <amount> &8- &fIncrease a bounty amount.".msg(sender)
        "&7/bounty cancel <bountyID> &8- &fCancel a bounty".msg(sender)
    }

}