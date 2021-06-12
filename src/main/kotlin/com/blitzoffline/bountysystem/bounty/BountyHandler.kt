package com.blitzoffline.bountysystem.bounty

import com.blitzoffline.bountysystem.BountySystem
import com.blitzoffline.bountysystem.config.holder.Bounties


class BountyHandler(plugin: BountySystem) {
    private val settings = plugin.settings
    
    val bounties = mutableListOf<Bounty>()

    fun getRandomId(): Short {
        val existentIds = bounties.map { it.id }
        val unusedIds = (1000..Short.MAX_VALUE).map { it.toShort() }.filter { !existentIds.contains(it) }
        return if (unusedIds.isEmpty()) 0.toShort() else unusedIds.random()
    }

    fun expired(bounty: Bounty) = System.currentTimeMillis() - bounty.placedTime >= settings[Bounties.EXPIRY_TIME] * 1000
}