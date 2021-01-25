package me.blitzgamer_88.bountysystem.commands

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.util.*
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
        if (!BountySystem.BOUNTIES_LIST.keys.contains(bountyId)) {
            sender.sendMessage(bountyNotFound.replace("%bountyID%", bountyId))
            return
        }

        val bounty = BountySystem.BOUNTIES_LIST[bountyId] ?: run {
            bountyNotFound.msg(sender)
            return
        }

        val payerOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.payer))
        econ.depositPlayer(payerOfflinePlayer, bounty.amount.toDouble())
        BountySystem.BOUNTIES_LIST.remove(bountyId)

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