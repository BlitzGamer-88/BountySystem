package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.util.createGUI
import com.blitzoffline.bountysystem.util.msg
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command("bounty")
class CommandBounty(private val plugin: BountySystem) : CommandBase() {
    private val messages = plugin.messages

    @Default
    @Permission("bountysystem.open")
    fun default(sender: Player) {
        if (plugin.bountyHandler.BOUNTIES.isEmpty()) {
            messages[Messages.NO_BOUNTIES_FOUND].msg(sender)
            return
        }
        createGUI(plugin).open(sender)
    }
}