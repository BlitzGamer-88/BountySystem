package com.blitzoffline.bountysystem.bounty

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
}