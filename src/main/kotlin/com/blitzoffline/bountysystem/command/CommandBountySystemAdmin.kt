package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.config.perms
import com.blitzoffline.bountysystem.util.msg
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
        if (!BOUNTIES_LIST.keys.contains(bountyId)) {
            sender.sendMessage(messages[Messages.BOUNTY_NOT_FOUND].replace("%bountyID%", bountyId))
            return
        }

        val bounty = BOUNTIES_LIST[bountyId] ?: run {
            messages[Messages.BOUNTY_NOT_FOUND].msg(sender)
            return
        }

        val payerOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.payer))
        econ.depositPlayer(payerOfflinePlayer, bounty.amount.toDouble())
        BOUNTIES_LIST.remove(bountyId)

        messages[Messages.BOUNTY_CANCELED].replace("%bountyId%", bountyId).msg(sender)
        payerOfflinePlayer.player?.let {
            messages[Messages.BOUNTY_CANCELED_ADMIN].replace("%bountyId%", bountyId).msg(it)
        }
    }

    @SubCommand("bypass")
    @Permission("bountysystem.admin.bypass", "bountysystem.admin")
    fun adminBypass(sender: CommandSender, @Completion("#players") player: Player) {
        perms.playerAdd(player, "bountysystem.bypass")
        messages[Messages.PLAYER_WHITELISTED].msg(sender)
    }

    @SubCommand("reload")
    @Permission("bountysystem.admin.reload", "bountysystem.admin")
    fun adminReload(sender: CommandSender) {
        plugin.reload()
        messages[Messages.CONFIG_RELOADED].msg(sender)
    }
}