package com.blitzoffline.bountysystem.task

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.util.msg
import org.bukkit.scheduler.BukkitRunnable

class BountyExpire : BukkitRunnable() {
    override fun run() {
        for (bounty in BOUNTIES_LIST) {
            if (!bounty.expired()) continue

            econ.depositPlayer(bounty.payer(), bounty.amount.toDouble())
            BOUNTIES_LIST.remove(bounty)

            bounty.payer().player?.let {
                messages[Messages.BOUNTY_EXPIRED]
                    .replace("%bountyId%", "${bounty.id}")
                    .msg(it)
            }
        }
    }
}