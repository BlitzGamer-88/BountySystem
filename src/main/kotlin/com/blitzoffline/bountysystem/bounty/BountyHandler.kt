package com.blitzoffline.bountysystem.bounty

//val BOUNTIES_LIST = mutableMapOf<String, Bounty>()

val BOUNTIES_LIST = mutableListOf<Bounty>()

fun getRandomId(): Short {
//    val existentIDs = BOUNTIES_LIST.keys.map { it.toShort() }
    val existentIDs = BOUNTIES_LIST.map { it.id }
    val unusedIds = (1000..Short.MAX_VALUE).map { it.toShort() }.filter { !existentIDs.contains(it) }
    return if (unusedIds.isEmpty()) 0.toShort() else unusedIds.random()
}