package me.blitzgamer_88.bountysystem.util

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.conf.Config
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class BountyExpire(private val plugin: BountySystem) : BukkitRunnable() {

    override fun run() {

        val bountyExpired = conf().getProperty(Config.bountyExpired)
        val bountyExpiryTime = conf().getProperty(Config.bountyExpiryTime).toLong()

        val bounties = plugin.getBounties()
        val ids = bounties.getKeys(false)

        val currentTimeInSeconds = System.currentTimeMillis() / 1000

        for (id in ids) {

            val newId = id.toIntOrNull() ?: continue
            if (newId < minId || newId > maxId) continue

            val placedTime = bounties.getLong("$id.placedTime")

            val payerUniqueIdString = bounties.getString("$id.placer") ?: return
            val payerUniqueId = UUID.fromString(payerUniqueIdString)
            val payerOfflinePlayer = Bukkit.getOfflinePlayer(payerUniqueId)
            val payerOnlinePlayer = payerOfflinePlayer.player

            if (currentTimeInSeconds - placedTime < bountyExpiryTime) return

            val amount = bounties.getInt("$id.amount")
            econ?.depositPlayer(payerOfflinePlayer, amount.toDouble())
            bounties.set(id, null)
            plugin.saveBounties()
            if (payerOnlinePlayer != null) bountyExpired.msg(payerOnlinePlayer)

        }
    }
}