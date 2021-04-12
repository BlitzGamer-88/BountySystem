package com.blitzoffline.bountysystem.runnable

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.util.*
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class BountyExpire(private val plugin: BountySystem) : BukkitRunnable() {
    override fun run() {
        val currentTimeInSeconds = System.currentTimeMillis() / 1000
        for (bounty in plugin.BOUNTIES_LIST.values) {
            val bountyId = bounty.id
            if (bountyId< minId || bountyId > maxId) continue
            if (currentTimeInSeconds - bounty.placedTime < bountyExpiryTime) return

            val payerOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.payer))
            econ.depositPlayer(payerOfflinePlayer, bounty.amount.toDouble())
            plugin.BOUNTIES_LIST.remove(bountyId.toString())

            payerOfflinePlayer.player?.let {
                bountyExpired.replace("%bountyId%", bountyId.toString()).msg(it)
            }
        }
    }
}