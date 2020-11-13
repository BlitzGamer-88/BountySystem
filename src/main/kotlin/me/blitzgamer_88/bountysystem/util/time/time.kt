package me.blitzgamer_88.bountysystem.util.time

fun formatTime(totalSeconds: Long) : String {

    val totalMinutes = totalSeconds/60
    val totalHours = totalMinutes/60

    val days = totalHours/24
    val hours = totalHours%24
    val minutes = totalMinutes%60
    val seconds = totalSeconds%60

    return days.toString() + "d " + hours.toString() + "h " + minutes.toString() + "m " + seconds.toString() + "s"
}