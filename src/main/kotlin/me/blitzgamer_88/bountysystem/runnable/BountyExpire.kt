package me.blitzgamer_88.bountysystem.runnable

import me.blitzgamer_88.bountysystem.BountySystem
import me.blitzgamer_88.bountysystem.conf.Config
import me.blitzgamer_88.bountysystem.util.chat.msg
import me.blitzgamer_88.bountysystem.util.conf.conf
import me.blitzgamer_88.bountysystem.util.conf.econ
import me.blitzgamer_88.bountysystem.util.conf.maxId
import me.blitzgamer_88.bountysystem.util.conf.minId
import me.blitzgamer_88.bountysystem.util.gui.updateGui
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class BountyExpire(private val plugin: BountySystem) : BukkitRunnable() {

    override fun run() {

        val bountyExpired = conf().getProperty(Config.bountyExpired)
        val bountyExpiryTime = conf().getProperty(Config.bountyExpiryTime).toLong()

        val bounties = plugin.BOUNTIES_LIST
        val ids = plugin.BOUNTIES_LIST.keys

        val currentTimeInSeconds = System.currentTimeMillis() / 1000

        for (id in ids) {

            val newId = id.toIntOrNull() ?: continue
            if (newId < minId || newId > maxId) continue

            val bounty = bounties[id] ?: return
            val placedTime = bounty.placedTime ?: return

            val payerUniqueIdString = bounty.payer ?: return
            val payerUniqueId = UUID.fromString(payerUniqueIdString)
            val payerOfflinePlayer = Bukkit.getOfflinePlayer(payerUniqueId)
            val payerOnlinePlayer = payerOfflinePlayer.player

            if (currentTimeInSeconds - placedTime < bountyExpiryTime) return

            val amount = bounty.amount ?: return

            econ?.depositPlayer(payerOfflinePlayer, amount.toDouble())
            plugin.BOUNTIES_LIST.remove(id)
            if (payerOnlinePlayer != null) bountyExpired.msg(payerOnlinePlayer)
        }
        updateGui(plugin)
    }
}