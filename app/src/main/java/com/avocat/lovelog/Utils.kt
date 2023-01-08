package com.avocat.lovelog

import android.annotation.SuppressLint
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.time.Period
import java.util.Date
import kotlin.math.abs


class Utils {
    companion object {
        const val COUPLE_DATE = "CoupleDate"
        const val LEFT_PARTNER = "LeftPartner"
        const val RIGHT_PARTNER = "RightPartner"
        const val PASSWORD = "Password"
        const val PASSWORD2REM = "Password2Rem"

        @SuppressLint("SimpleDateFormat")
        fun getDate(preferences: SharedPreferences): Date? {
            val date = preferences.getString(COUPLE_DATE, null)
            var result: Date? = null
            date?.let {
                result = SimpleDateFormat("dd.MM.yyyy").parse(date)!!
            }
            return result
        }

        fun getCouple(preferences: SharedPreferences): List<String> {
            val left = preferences.getString(LEFT_PARTNER, "")!!
            val right = preferences.getString(RIGHT_PARTNER, "")!!
            return listOf(left, right)
        }

        @SuppressLint("SimpleDateFormat")
        fun getPeriod(date: String): List<Long> {
            val start: Date = SimpleDateFormat("dd.MM.yyyy").parse(date)!!
            val end = Date()
            val diff = abs(end.time - start.time)
            val days = diff / 1000 / 60 / 60 / 24
            val months = days / 30
            val years = days / 365
            println(start)
            println(end)
            println(date)
            println(years)
            println(months)
            println(days)
            return listOf(years, months % 12, (days % 365) % 30, days)
        }
    }
}
