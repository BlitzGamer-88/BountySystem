package com.blitzoffline.bountysystem.util

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

lateinit var adventure: BukkitAudiences


val specialSerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build()
val legacySerializer = LegacyComponentSerializer.legacyAmpersand()

fun String.color() = specialSerializer.serialize(legacySerializer.deserialize(this))
fun List<String>.color() = map { it.color() }

fun String.parsePAPI(player: Player) = PlaceholderAPI.setPlaceholders(player, this)
fun String.parsePAPI(player: OfflinePlayer) = PlaceholderAPI.setPlaceholders(player, this)

fun String.log() = Bukkit.getConsoleSender().sendMessage(this.color())

fun String.msg(player: Player) = adventure.player(player).sendMessage(legacySerializer.deserialize(this))
fun String.msg(sender: CommandSender) = adventure.sender(sender).sendMessage(legacySerializer.deserialize(this))
fun String.broadcast() = adventure.all().sendMessage(legacySerializer.deserialize(this))