package com.blitzoffline.bountysystem.util

import com.blitzoffline.bountysystem.config.holder.Messages
import com.blitzoffline.bountysystem.config.messages

fun formatTime(totalSeconds: Long) : String {
    val totalMinutes = totalSeconds/60
    val totalHours = totalMinutes/60

    val days = totalHours/24
    val hours = totalHours%24
    val minutes = totalMinutes%60
    val seconds = totalSeconds%60

    return days.toString() + messages[Messages.TIME_DAYS] + hours.toString() + messages[Messages.TIME_HOURS] + minutes.toString() + messages[Messages.TIME_MINUTES] + seconds.toString() + messages[Messages.TIME_SECONDS]
}