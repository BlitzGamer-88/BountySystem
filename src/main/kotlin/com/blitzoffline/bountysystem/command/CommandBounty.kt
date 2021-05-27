package com.blitzoffline.bountysystem.command

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.util.createGUI
import com.blitzoffline.bountysystem.util.msg
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command("bounty")
class CommandBounty : CommandBase() {

    @Default
    @Permission("bountysystem.open")
    fun default(sender: Player) {
        if (BOUNTIES_LIST.isEmpty()) {
            messages[Messages.NO_BOUNTIES_FOUND].msg(sender)
            return
        }
        createGUI().open(sender)
    }

}