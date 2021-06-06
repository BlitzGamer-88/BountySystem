package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.config.holder.Messages
import me.mattstudios.config.SettingsManager

fun Long.format(messages: SettingsManager) : String {
    val totalSeconds = this

    val totalMinutes = totalSeconds/60
    val totalHours = totalMinutes/60

    val days = totalHours/24
    val hours = totalHours%24
    val minutes = totalMinutes%60
    val seconds = totalSeconds%60

    return StringBuilder()
        .append(days).append(messages[Messages.TIME_DAYS])
        .append(hours).append(messages[Messages.TIME_HOURS])
        .append(minutes).append(messages[Messages.TIME_MINUTES])
        .append(seconds).append(messages[Messages.TIME_SECONDS])
        .toString()
}