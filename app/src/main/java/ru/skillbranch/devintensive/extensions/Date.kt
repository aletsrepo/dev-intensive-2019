package ru.skillbranch.devintensive.extensions

import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

enum class TimeUnits(
    val value: Long,
    private val first: String,
    private val second: String,
    private val other: String
) {
    SECOND(1000L, "секунду", "секунды", "секунд"),
    MINUTE(60 * SECOND.value, "минуту", "минуты", "минут"),
    HOUR(60 * MINUTE.value, "час", "часа", "часов"),
    DAY(24 * HOUR.value, "день", "дня", "дней");

    fun plural(value: Int): String {
        val preLastDigit = value % 100 / 10

        if (preLastDigit == 1) {
            return "$value ${this.other}"
        }

        return when (value % 10) {
            1 -> "$value ${this.first}"
            2, 3, 4 -> "$value ${this.second}"
            else -> "$value ${this.other}"
        }
    }
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, unit: TimeUnits): Date {
    var time = this.time
    time += when (unit) {
        TimeUnits.SECOND -> value * unit.value
        TimeUnits.MINUTE -> value * unit.value
        TimeUnits.HOUR -> value * unit.value
        TimeUnits.DAY -> value * unit.value
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val second = TimeUnits.SECOND.value
    val minute = TimeUnits.MINUTE.value
    val hour = TimeUnits.HOUR.value
    val day = TimeUnits.DAY.value
    val seenTimeMs = date.time - this.time
    return when (abs(seenTimeMs)) {
        in 0..second -> "только что"
        in second..45 * second -> if (seenTimeMs < 0) "через несколько секунд" else "несколько секунд назад"
        in 45 * second..75 * second -> if (seenTimeMs < 0) "через минуту" else "минуту назад"
        in 75 * second..45 * minute -> {
            val plural = TimeUnits.MINUTE.plural((abs(seenTimeMs) / minute).toInt())
            if (seenTimeMs < 0) "через $plural" else "$plural назад"
        }
        in 45 * minute..75 * minute -> if (seenTimeMs < 0) "через час" else "час назад"
        in 75 * minute..22 * hour -> {
            val plural = TimeUnits.HOUR.plural((abs(seenTimeMs) / hour).toInt())
            if (seenTimeMs < 0) "через $plural" else "$plural назад"
        }
        in 22 * hour..26 * hour -> if (seenTimeMs < 0) "через день" else "день назад"
        in 26 * hour..360 * day -> {
            val plural = TimeUnits.DAY.plural((abs(seenTimeMs) / day).toInt())
            if (seenTimeMs < 0) "через $plural" else "$plural назад"
        }
        in 360 * day..Long.MAX_VALUE -> if (seenTimeMs < 0) "более чем через год" else "более года назад"
        else -> throw IllegalArgumentException("Invalid argument")
    }
}
