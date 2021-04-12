package com.blitzoffline.bountysystem.runnable

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.econ
import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages
import com.blitzoffline.bountysystem.config.settings
import com.blitzoffline.bountysystem.util.msg
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

const val minId: Short = 1000

class BountyExpire : BukkitRunnable() {
    override fun run() {
        val currentTimeInSeconds = System.currentTimeMillis() / 1000
        for (bounty in BOUNTIES_LIST.values) {
            val bountyId = bounty.id
            if (bountyId < minId) continue
            if (currentTimeInSeconds - bounty.placedTime < settings[Bounties.EXPIRY_TIME]) continue

            val payerOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.payer))
            econ.depositPlayer(payerOfflinePlayer, bounty.amount.toDouble())
            BOUNTIES_LIST.remove(bountyId.toString())

            payerOfflinePlayer.player?.let {
                messages[Messages.BOUNTY_EXPIRED]
                    .replace("%bountyId%", bountyId.toString())
                    .msg(it)
            }
        }
    }
}