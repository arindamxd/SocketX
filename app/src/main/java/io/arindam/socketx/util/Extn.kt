package io.arindam.socketx.util

import android.content.Context
import androidx.core.content.ContextCompat
import io.arindam.socketx.R
import java.util.*

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

fun Double.getAQI(): String = "%.2f".format(this)
fun Double.getColorCode(context: Context): Int = when {
    this <= 50 -> ContextCompat.getColor(context, R.color.good)
    this <= 100 -> ContextCompat.getColor(context, R.color.satisfactory)
    this <= 200 -> ContextCompat.getColor(context, R.color.moderate)
    this <= 300 -> ContextCompat.getColor(context, R.color.poor)
    this <= 400 -> ContextCompat.getColor(context, R.color.very_poor)
    else -> ContextCompat.getColor(context, R.color.severe)
}

fun Long.getMinute(): Float {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.SECOND).toFloat()
}

fun Long.getTime(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val second = calendar.get(Calendar.SECOND)

    val currentCalendar = Calendar.getInstance()
    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
    val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = currentCalendar.get(Calendar.MINUTE)
    val currentSecond = currentCalendar.get(Calendar.SECOND)

    return when {
        year < currentYear -> {
            val interval = currentYear - year
            if (interval == 1) "$interval year ago" else "$interval years ago"
        }
        month < currentMonth -> {
            val interval = currentMonth - month
            if (interval == 1) "$interval month ago" else "$interval months ago"
        }
        day < currentDay -> {
            val interval = currentDay - day
            if (interval == 1) "$interval day ago" else "$interval days ago"
        }
        hour < currentHour -> {
            val interval = currentHour - hour
            if (interval == 1) "$interval hour ago" else "$interval hours ago"
        }
        minute < currentMinute -> {
            val interval = currentMinute - minute
            if (interval == 1) "$interval minute ago" else "$interval minutes ago"
        }
        second < currentSecond -> {
            val interval = currentSecond - second
            if (interval == 1) "a moment ago" else "a few seconds ago"
        }
        else -> "a moment ago"
    }
}

