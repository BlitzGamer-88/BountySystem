package com.blitzoffline.bountysystem.bounty

import com.blitzoffline.bountysystem.config.holder.Bounties
import com.blitzoffline.bountysystem.config.settings
import java.util.UUID
import org.bukkit.Bukkit

class Bounty(
    var id: Short,
    var payer: UUID,
    var target: UUID,
    var amount: Int,
    var placedTime: Long
    )
{
    fun payer() = Bukkit.getOfflinePlayer(payer)
    fun target() = Bukkit.getOfflinePlayer(target)
    fun expired() = System.currentTimeMillis() - placedTime >= settings[Bounties.EXPIRY_TIME] * 1000
    fun afterTax() = amount - ((settings[Bounties.TAX] / 100) * amount)
}