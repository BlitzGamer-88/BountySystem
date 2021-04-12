package com.blitzoffline.bountysystem.util

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

lateinit var adventure: BukkitAudiences

fun String.color(): String = ChatColor.translateAlternateColorCodes('&', this)

fun MutableList<String>.color(): MutableList<String> {
    val result = mutableListOf<String>()
    for(line in this) {
        result.add(line.color())
    }
    return result
}

fun String.parsePAPI(player: Player): String = PlaceholderAPI.setPlaceholders(player, this.color())
fun String.parsePAPI(player: OfflinePlayer): String = PlaceholderAPI.setPlaceholders(player, this.color())

fun String.log() = Bukkit.getConsoleSender().sendMessage(this.color())

fun String.msg(player: Player) = adventure.player(player).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(this))
fun String.msg(sender: CommandSender) = adventure.sender(sender).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(this))
fun String.broadcast() = adventure.all().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(this))