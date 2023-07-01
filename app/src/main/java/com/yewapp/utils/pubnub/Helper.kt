package com.yewapp.utils.pubnub

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.yewapp.MainApplication
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    fun Helper() {}

    companion object {
        fun <T> getRandomElement(array: Array<T>): T {
            return array[(Math.random() * array.size + 1).toInt()]
        }

        fun <T> getRandomElement(array: List<T>): T {
            return array[(Math.random() * array.size).toInt()]
        }

        fun md5(input: String): String? {
            return try {
                val md = MessageDigest.getInstance("MD5")
                val messageDigest = md.digest(input.toByteArray())
                val no = BigInteger(1, messageDigest)
                var hashtext = no.toString(16)
                while (hashtext.length < 32) {
                    hashtext = "0$hashtext"
                }
                hashtext
            } catch (e: NoSuchAlgorithmException) {
                ""
            }
        }

        fun parseTime(timetoken: Long): String? {
            @SuppressLint("SimpleDateFormat") val calendar = Calendar.getInstance()
            calendar.timeInMillis = timetoken
            calendar.timeZone = TimeZone.getTimeZone("UTC")
            return DateFormat.getTimeFormat(MainApplication.mContext).format(calendar.time)
        }

        fun parseDateTimeIso8601(timetoken: Long): String? {
            @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timetoken
            return sdf.format(calendar.time)
        }

        fun parseDateTime(timetoken: Long, format: String?): String? {
            @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat(format)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timetoken
            return sdf.format(calendar.time)
        }


        fun trimTime(timetoken: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timetoken
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.timeInMillis
        }

        fun getRelativeTime(timetoken: Long): String? {
            return DateUtils.getRelativeTimeSpanString(
                timetoken, Calendar.getInstance()
                    .timeInMillis, 0L, DateUtils.FORMAT_ABBREV_ALL
            ) as String
        }
    }
}