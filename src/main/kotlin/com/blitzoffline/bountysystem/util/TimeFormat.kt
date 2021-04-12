package com.blitzoffline.bountysystem.util

fun formatTime(totalSeconds: Long) : String {
    val totalMinutes = totalSeconds/60
    val totalHours = totalMinutes/60

    val days = totalHours/24
    val hours = totalHours%24
    val minutes = totalMinutes%60
    val seconds = totalSeconds%60

    return days.toString() + daysFormat + hours.toString() + hoursFormat + minutes.toString() + minutesFormat + seconds.toString() + secondsFormat
}