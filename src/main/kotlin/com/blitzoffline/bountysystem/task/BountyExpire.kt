package com.blitzoffline.bountysystem.task

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.util.msg
import org.bukkit.scheduler.BukkitRunnable

class BountyExpire(private val plugin: BountySystem) : BukkitRunnable() {
    private val messages = plugin.messages

    override fun run() {
        for (bounty in plugin.bountyHandler.bounties) {
            if (!plugin.bountyHandler.expired(bounty)) continue

            plugin.economy.depositPlayer(bounty.payer(), bounty.amount.toDouble())
            plugin.bountyHandler.bounties.remove(bounty)

            bounty.payer().player?.let {
                messages[Messages.BOUNTY_EXPIRED]
                    .replace("%bountyId%", "${bounty.id}")
                    .msg(it)
            }
        }
    }
}