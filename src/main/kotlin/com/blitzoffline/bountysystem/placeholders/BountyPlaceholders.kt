package com.blitzoffline.bountysystem.placeholders

import com.blitzoffline.bountysystem.BountySystem
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class BountyPlaceholders(private val plugin: BountySystem) : PlaceholderExpansion() {
    override fun getIdentifier() = "bountysystem"

    override fun getAuthor() = plugin.description.authors[0] ?: "BlitzOffline"

    override fun getVersion() = plugin.description.version

    override fun canRegister() = true

    override fun persist() = true

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        // TODO: Add placeholders
        return null
    }
}