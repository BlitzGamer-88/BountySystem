package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.config.holder.Messages
import me.mattstudios.config.SettingsManager

fun Long.format(messages: SettingsManager) : String {
    val totalSeconds = this

    val totalMinutes = totalSeconds/60
    val totalHours = totalMinutes/60

    return StringBuilder()
        .append(totalHours/24).append(messages[Messages.TIME_DAYS])
        .append(totalHours%24).append(messages[Messages.TIME_HOURS])
        .append(totalMinutes%60).append(messages[Messages.TIME_MINUTES])
        .append(totalSeconds%60).append(messages[Messages.TIME_SECONDS])
        .toString()
}