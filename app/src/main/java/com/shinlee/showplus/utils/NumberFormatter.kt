package com.shinlee.showplus.utils

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
object NumberFormatter {
    fun formatSocialCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 1_000_000 -> String.format("%.1fK", count / 1000.0)
            else -> String.format("%.1fM", count / 1_000_000.0)
        }
    }
}