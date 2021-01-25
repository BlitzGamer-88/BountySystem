package me.blitzgamer_88.bountysystem.runnable

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.util.*
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class BountyExpire : BukkitRunnable() {
    override fun run() {
        val currentTimeInSeconds = System.currentTimeMillis() / 1000
        for (bounty in BountySystem.BOUNTIES_LIST.values) {
            val bountyId = bounty.id
            if (bountyId< minId || bountyId > maxId) continue
            if (currentTimeInSeconds - bounty.placedTime < bountyExpiryTime) return

            val payerOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(bounty.payer))
            econ.depositPlayer(payerOfflinePlayer, bounty.amount.toDouble())
            BountySystem.BOUNTIES_LIST.remove(bountyId.toString())

            payerOfflinePlayer.player?.let {
                bountyExpired.replace("%bountyId%", bountyId.toString()).msg(it)
            }
        }
    }
}