package com.blitzoffline.bountysystem.runnable

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.config.holder.Bounty
import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.util.*
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

private const val minId = 1000
private const val maxId = 9999

class BountyExpire(private val plugin: BountySystem) : BukkitRunnable() {
    override fun run() {
        val config = plugin.config
        val messages = plugin.messages

        val currentTimeInSeconds = System.currentTimeMillis() / 1000
        for (bounty in BOUNTIES_LIST.values) {
            val bountyId = bounty.id
            if (bountyId< minId || bountyId > maxId) continue
            if (currentTimeInSeconds - bounty.placedTime < config[Bounty.EXPIRY_TIME]) return

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