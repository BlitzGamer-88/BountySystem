package com.blitzoffline.bountysystem.bounty

const val minId: Short = 1000
val BOUNTIES_LIST = mutableMapOf<String, Bounty>()

fun getRandomId(): Short {
    val existentIDs = BOUNTIES_LIST.keys.map { it.toShort() }
    val unusedIds = (minId..Short.MAX_VALUE).map { it.toShort() }.filter { !existentIDs.contains(it) }
    return if (unusedIds.isEmpty()) 0.toShort() else unusedIds.random()
}