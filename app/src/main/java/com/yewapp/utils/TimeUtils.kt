package com.yewapp.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

enum class DateRange {
    CURRENT, LAST_WEEK, LAST_MONTH, LAST_YEAR
}

fun String.parse(inputFormat: String = "hh:mm a", outputFormat: String = "HH:mm:ss.SSS"): String? {
    val sdf = SimpleDateFormat(inputFormat, Locale.ENGLISH);
    val dateObj = sdf.parse(this) ?: return null
    val timeZone = TimeZone.getTimeZone("UTC")
    return SimpleDateFormat(outputFormat).apply {
        this.timeZone = timeZone
    }.format(dateObj)
}

fun String.isBefore(compareWith: String, inputFormat: String = "hh:mm a"): Boolean {
    val sdf = SimpleDateFormat(inputFormat, Locale.getDefault())
    val dateBefore = sdf.parse(this)
    val dateAfter = sdf.parse(compareWith)

    return dateBefore.before(dateAfter)
}

fun getTimeZoneOffset(): String {
    val format = SimpleDateFormat("Z", Locale.getDefault()).format(Date())

    val sb = StringBuilder(format)
    sb.insert(3, ":")
    return sb.toString()
}

//fun getCurrentDate(format: String = "yyyy-mm-dd"): String {
//    val sdf = SimpleDateFormat(format, Locale.getDefault())
//    val date = Calendar.getInstance()
//    return sdf.format(date)
//}

fun String.getDate(format: String = "dd-mm-yyyy"): Date? {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.parse(this)
}

fun String.parseToUTC(format: String = "yyyy-MM-dd"): String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = this.toLong()

    val locale: Locale = Locale.getDefault()
    val df = SimpleDateFormat(format, locale)
    val timeZone = TimeZone.getTimeZone("UTC")
    df.timeZone = timeZone

    val d: Date = calendar.time
    return df.format(d)
}

fun String.convertDate(
    inputFormat: String = "dd/MM/yyyy",
    outputFormat: String = "yyyy-MM-dd"
): String {
    val inputFormatter = DateTimeFormat.forPattern(inputFormat)
    val outputFormatter = DateTimeFormat.forPattern(outputFormat)

    val date = inputFormatter.parseDateTime(this)
    return outputFormatter.print(date)
}

fun is18(date1: DateTime, date2: DateTime): Boolean {
    val period = Period(date1, date2)
    return period.years >= 18
}

fun getDateTime(date: String): DateTime {
    if (date.isEmpty()) return DateTime()
    val dateFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")
    return dateFormatter.parseDateTime(date)
}

fun getFeedTime(millis: Long): String {

    val date = Date(millis * 1000)
    val format: SimpleDateFormat = SimpleDateFormat("EEE dd, hh:mm a")
    format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"))
    var formatted: String? = format.format(date)

    format.setTimeZone(TimeZone.getDefault())

    val timeDifference = timeDifference(date)
    formatted = format.format(date)
    return timeDifference ?: formatted
}

fun timeDifference(createdDate: Date): String? {
    val now = Date()
    val difference = abs(createdDate.time - now.time)
    val seconds = TimeUnit.SECONDS.convert(difference, TimeUnit.MILLISECONDS)
    val days = TimeUnit.DAYS.convert(difference, TimeUnit.DAYS)
    return when {
        seconds < 60 -> "$seconds secs ago"
        seconds / 60 < 60 -> "${seconds / 60} mins ago"
        seconds / 3600 < 24 -> "${seconds / 3600} hrs ago"
        seconds / (3600 * 24) < 30 -> "${seconds / (3600 * 24)} days ago"
        seconds / (3600 * 24 * 30) < 12 -> "${seconds / (3600 * 24 * 30)} months ago"
        else -> "${seconds / (3600 * 24 * 30 * 12)} years ago"
    }
}

fun getCurrentDate(): String {
    val date = DateTime().millis
    val format: SimpleDateFormat = SimpleDateFormat("EEE dd, hh:mm a")
    format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"))
    var formatted: String? = format.format(date)

    format.setTimeZone(TimeZone.getDefault())

    formatted = format.format(date)
    return formatted
}

fun getLastWeek(): Pair<String, String> {
    val date = DateTime().minusWeeks(1)

    val startDate = date.withDayOfWeek(DateTimeConstants.MONDAY)
    val endDate = date.withDayOfWeek(DateTimeConstants.SUNDAY)

    return Pair(startDate.toString("yyyy-MM-dd"), endDate.toString("yyyy-MM-dd"))
}

fun getLastMonth(): Pair<String, String> {
    val date = DateTime().minusMonths(1)
    val startDate = date.dayOfMonth().withMinimumValue()
    val endDate = date.dayOfMonth().withMaximumValue()
    return Pair(startDate.toString("yyyy-MM-dd"), endDate.toString("yyyy-MM-dd"))
}

fun getLastYear(): Pair<String, String> {
    val date = DateTime().minusYears(1)
    val startDate = date.dayOfYear().withMinimumValue()
    val endDate = date.dayOfYear().withMaximumValue()
    return Pair(startDate.toString("yyyy-MM-dd"), endDate.toString("yyyy-MM-dd"))
}

fun getDateRange(range: DateRange): Pair<String, String> {
    var date = DateTime()
    var startDate: DateTime? = null
    var endDate: DateTime? = null
    when (range) {
        DateRange.CURRENT -> {
            startDate = date
            endDate = date
        }
        DateRange.LAST_WEEK -> {
            val date = date.minusWeeks(1)
            startDate = date.dayOfWeek().withMinimumValue()
            endDate = date.dayOfWeek().withMaximumValue()
        }
        DateRange.LAST_MONTH -> {
            date = DateTime().minusMonths(1)
            startDate = date.dayOfMonth().withMinimumValue()
            endDate = date.dayOfMonth().withMaximumValue()
        }
        DateRange.LAST_YEAR -> {
            date = DateTime().minusYears(1)
            startDate = date.dayOfYear().withMinimumValue()
            endDate = date.dayOfYear().withMaximumValue()
        }
    }

    return Pair(startDate?.toString("yyyy-MM-dd") ?: "", endDate?.toString("yyyy-MM-dd") ?: "")
}

