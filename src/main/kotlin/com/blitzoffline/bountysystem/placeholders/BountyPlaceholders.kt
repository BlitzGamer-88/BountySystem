package com.blitzoffline.bountysystem.placeholders

import com.blitzoffline.bountysystem.BountySystem
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class BountyPlaceholders(private val plugin: BountySystem) : PlaceholderExpansion() {

    override fun getIdentifier() = plugin.description.name.lowercase()

    override fun getVersion() = plugin.description.version

    override fun getAuthor() = "BlitzOffline"

    override fun canRegister() = true

    override fun persist() = true

    override fun onRequest(player: OfflinePlayer?, params: String) =
        when {
            params.split("_").size != 2 -> null

            params.startsWith("target_", true) -> {
                val id = params.substringAfter("target_")
                val bounty = plugin.bountyHandler.BOUNTIES.firstOrNull { it.id.toString() == id && !plugin.bountyHandler.expired(it) } ?: run { return "" }
                bounty.target().name ?: ""
            }

            params.startsWith("payer_", true) -> {
                val id = params.substringAfter("payer_")
                val bounty = plugin.bountyHandler.BOUNTIES.firstOrNull { it.id.toString() == id && !plugin.bountyHandler.expired(it) } ?: run { return "" }
                bounty.payer().name ?: ""
            }

            params.startsWith("amount_", true) -> {
                val id = params.substringAfter("amount_")
                val bounty = plugin.bountyHandler.BOUNTIES.firstOrNull { it.id.toString() == id && !plugin.bountyHandler.expired(it) } ?: run { return "" }
                bounty.amount.toString()
            }

            params.startsWith("ids_", true) -> {
                val p = plugin.server.getOfflinePlayerIfCached(params.substringAfter("ids_")) ?: run { return "" }
                val ids = plugin.bountyHandler.BOUNTIES.filter { !plugin.bountyHandler.expired(it) && it.payer() != p }.map { it.id }.ifEmpty { return "" }
                ids.joinToString(", ")
            }

            else -> null
        }
}