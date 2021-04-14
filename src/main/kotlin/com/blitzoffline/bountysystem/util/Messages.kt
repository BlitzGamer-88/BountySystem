package com.blitzoffline.bountysystem.util

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

lateinit var adventure: BukkitAudiences

fun String.log() = Bukkit.getConsoleSender().sendMessage(this.color())
fun String.msg(player: Player) = adventure.player(player).sendMessage(legacySerializer.deserialize(this))
fun String.msg(sender: CommandSender) = adventure.sender(sender).sendMessage(legacySerializer.deserialize(this))
fun String.broadcast() = adventure.all().sendMessage(legacySerializer.deserialize(this))