package com.shinlee.showplus.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object TimeUtils {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.KOREAN).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun getTimeAgo(dateString: String): String {
        try {
            val past = dateFormat.parse(dateString)?.time ?: return "invalid date"
            val now = System.currentTimeMillis()

            val difference = now - past
            val seconds = difference / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val months = days / 30
            val years = days / 365

            return when {
                seconds < 0 -> "in the future"
                seconds < 60 -> "just now"
                minutes < 60 -> "${minutes}m"
                hours < 24 -> "${hours}h"
                days < 7 -> "${days}d"
                days < 30 -> "${days / 7}w"
                months < 12 -> "${months}mo"
                else -> "${years}y"
            }
        } catch (e: Exception) {
            return "invalid date"
        }
    }
}
