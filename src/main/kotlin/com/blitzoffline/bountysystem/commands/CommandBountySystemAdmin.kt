package com.blitzoffline.bountysystem.commands

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.util.*
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@Alias("badmin")
@Command("bountyadmin")
class CommandBountySystemAdmin(private val plugin: BountySystem) : CommandBase() {

    @SubCommand("cancel")
    @Permission("bountysystem.admin.cancel", "bountysystem.admin")
    fun adminCancel(sender: CommandSender, bountyId: String) {
        if (!plugin.BOUNTIES_LIST.keys.contains(bountyId)) {
            sender.sendMessage(bountyNotFound.replace("%bountyID%", bountyId))
            return
        }

        val bounty = plugin.BOUNTIES_LIST[bountyId] ?: run {
            bountyNotFound.msg(sender)
            return
        }

        val payerOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.payer))
        econ.depositPlayer(payerOfflinePlayer, bounty.amount.toDouble())
        plugin.BOUNTIES_LIST.remove(bountyId)

        bountyCanceled.replace("%bountyId%", bountyId).msg(sender)
        payerOfflinePlayer.player?.let {
            bountyCanceledByAdmin.replace("%bountyId%", bountyId).msg(it)
        }
    }

    @SubCommand("bypass")
    @Permission("bountysystem.admin.bypass", "bountysystem.admin")
    fun adminBypass(sender: CommandSender, @Completion("#players") player: Player) {
        perms.playerAdd(player, "bountysystem.bypass")
        playerWhitelisted.msg(sender)
    }

    @SubCommand("reload")
    @Permission("bountysystem.admin.reload", "bountysystem.admin")
    fun adminReload(sender: CommandSender) {
        plugin.reload()
        configReloaded.msg(sender)
    }
}