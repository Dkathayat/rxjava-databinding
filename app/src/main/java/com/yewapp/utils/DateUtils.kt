package com.yewapp.utils

import android.annotation.SuppressLint
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {


    companion object {


        @SuppressLint("SimpleDateFormat")
        fun getChallengeDateFromTimeStamp(milliSeconds: Long): String {
            var dateFormat = ""
            if (dateFormat.trim() == "") {
                dateFormat = "dd MMM yyyy"
            }

            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat)
            formatter.timeZone = TimeZone.getDefault()
            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar: Calendar = Calendar.getInstance()
//            calendar.timeZone = TimeZone.getTimeZone("UTC")
            calendar.timeInMillis = milliSeconds * 1000


//            val timeD = Date(time * 1000)
//            calendar.time = Date(time * 1000)

            return formatter.format(calendar.time)
        }


        @SuppressLint("SimpleDateFormat")
        fun getDateFromTimeStamp(milliSeconds: Long): String {
            var dateFormat = ""
            if (dateFormat.trim() == "") {
                dateFormat = "E dd MMM yyyy"
            }

            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat)
            formatter.timeZone = TimeZone.getDefault()
            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar: Calendar = Calendar.getInstance()
//            calendar.timeZone = TimeZone.getTimeZone("UTC")
            calendar.timeInMillis = milliSeconds * 1000


//            val timeD = Date(time * 1000)
//            calendar.time = Date(time * 1000)

            return formatter.format(calendar.time)
        }

        @SuppressLint("SimpleDateFormat")
        fun getDateFromUTCDateTime(utcDateTime: String?, outputPattern: String): String? {
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
//            val outputPattern = "E dd MMM yyyy, hh:mm aa"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            outputFormat.timeZone = TimeZone.getDefault()
            var date: Date? = null
            var str: String? = null
            try {
                date = utcDateTime?.let { inputFormat.parse(it) }
                str = date?.let { outputFormat.format(it) }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }


        fun getDateTimeFromTimeStamp(timestamp: String?): String? {
            return try {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val netDate = Date(timestamp?.toLong()!! * 1000)
                sdf.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        }





        @SuppressLint("SimpleDateFormat")
        fun getRemainingTime(startDate: String?): String {
//            val lockDurationStartDate = startDate?.replace("\n", "")
            val simpleDateFormat = SimpleDateFormat("E dd MMM yyyy")
            simpleDateFormat.timeZone = TimeZone.getDefault()
            val currentDate = simpleDateFormat.format(Date())

            lateinit var date1: Date
            lateinit var date2: Date
            try {
                date1 = simpleDateFormat.parse(startDate)!!
                date2 = simpleDateFormat.parse(currentDate)!!
                return compareDateTime(date2, date1)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return "N/A"
        }

        @SuppressLint("SimpleDateFormat")
        fun convertDisplayDateToApiFormat(startDate: String): String? {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd")
            return inputFormat.parse(startDate)?.let { outputFormat.format(it) }
        }
        @SuppressLint("SimpleDateFormat")// 6 Feb 2023 ti 2023-02-06
        fun convertCreateChallengeDatesToApiFormat(startDate: String): String? {
            val inputFormat = SimpleDateFormat("dd MMM yyyy")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd")
            return inputFormat.parse(startDate)?.let { outputFormat.format(it) }
        }

        @SuppressLint("SimpleDateFormat")
        fun convertApiDateToDisplayFormat(startDate: String): String? {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd/MM/yyyy")
            return inputFormat.parse(startDate)?.let { outputFormat.format(it) }
        }


        private fun compareDateTime(date1: Date, date2: Date): String {
            //milliseconds
            var different = date2.time - date1.time
            println("startDate : $date1")
            println("endDate : $date2")
            println("different : $different")
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val elapsedDays = different / daysInMilli
            different %= daysInMilli
            val elapsedHours = different / hoursInMilli
            different %= hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different %= minutesInMilli
            val elapsedSeconds = different / secondsInMilli
            System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours,
                elapsedMinutes,
                elapsedSeconds
            )
            when {
                elapsedDays.toInt() > 0 -> {
                    return if (elapsedDays.toInt() == 1)
                        "$elapsedDays\nday"
                    else
                        "$elapsedDays\ndays"
                }
                elapsedHours.toInt() > 0 -> {
                    return if (elapsedHours.toInt() == 1)
                        "$elapsedHours\nhour"
                    else
                        "$elapsedHours\nhours"
                }
                elapsedMinutes.toInt() > 0 -> {
                    return "$elapsedMinutes\nmin"
                }
                else -> {
                    return "$elapsedSeconds\nsec"
                }
            }
        }
    }

}