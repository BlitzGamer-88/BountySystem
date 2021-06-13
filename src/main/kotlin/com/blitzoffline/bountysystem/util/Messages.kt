package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.BountySystem
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

val adventure: BukkitAudiences = BukkitAudiences.create(JavaPlugin.getPlugin(BountySystem::class.java))

fun String.sendMessage(player: Player) = adventure.player(player).sendMessage(legacySerializer.deserialize(this.parsePAPI(player)))
fun String.sendMessage(sender: CommandSender) = adventure.sender(sender).sendMessage(legacySerializer.deserialize(this))
fun String.broadcastMessage() = adventure.all().sendMessage(legacySerializer.deserialize(this))