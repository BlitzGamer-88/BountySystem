package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.BountySystem

fun getRandomId(plugin: BountySystem): Int {
    val ids = (minId..maxId).toMutableList()

    for (bounty in plugin.BOUNTIES_LIST.values) {
        if (bounty.id < minId || bounty.id > maxId) continue
        if (ids.contains(bounty.id)) ids.remove(bounty.id)
    }

    if (ids.isEmpty()) return 0
    return ids.random()
}