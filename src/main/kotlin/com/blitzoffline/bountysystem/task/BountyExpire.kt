package com.blitzoffline.bountysystem.task

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.bounty.minId
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.config.settings
import com.blitzoffline.bountysystem.util.msg
import org.bukkit.scheduler.BukkitRunnable

class BountyExpire : BukkitRunnable() {
    override fun run() {
        for (bounty in BOUNTIES_LIST.values) {
            if (bounty.id < minId) continue
            if (System.currentTimeMillis() - bounty.placedTime < settings[Bounties.EXPIRY_TIME] * 1000) continue

            econ.depositPlayer(bounty.payer(), bounty.amount.toDouble())
            BOUNTIES_LIST.remove("${bounty.id}")

            bounty.payer().player?.let {
                messages[Messages.BOUNTY_EXPIRED]
                    .replace("%bountyId%", "${bounty.id}")
                    .msg(it)
            }
        }
    }
}