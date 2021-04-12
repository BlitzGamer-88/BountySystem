package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.bounty.BOUNTIES_LIST
import com.blitzoffline.bountysystem.runnable.minId

fun getRandomId(): Short {
    val ids = (minId..Short.MAX_VALUE).map { it.toShort() }.toMutableList()

    for (bounty in BOUNTIES_LIST.values) {
        if (bounty.id < minId) continue
        if (ids.contains(bounty.id)) ids.remove(bounty.id)
    }

    if (ids.isEmpty()) return 0
    return ids.random()
}