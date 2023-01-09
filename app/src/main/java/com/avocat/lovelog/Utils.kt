package com.avocat.lovelog

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.decodeBitmap
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs


class Utils {
    companion object {
        const val COUPLE_DATE = "CoupleDate"
        const val LEFT_PARTNER = "LeftPartner"
        const val RIGHT_PARTNER = "RightPartner"
        const val LEFT_AVATAR = "LeftAvatar"
        const val RIGHT_AVATAR = "RightAvatar"
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

        fun getCouplePhotos(preferences: SharedPreferences): List<String> {
            val left = preferences.getString(LEFT_AVATAR, "")!!
            val right = preferences.getString(RIGHT_AVATAR, "")!!
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
            return listOf(years, months % 12, (days % 365) % 30, days)
        }

        fun uriToImageBitmap(uri: Uri, contentResolver: ContentResolver): ImageBitmap? {
            if (uri.toString().isEmpty())
                return null
            return try {
                when {
                    Build.VERSION.SDK_INT < 28 -> {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    }
                    else -> {
                        val source = ImageDecoder.createSource(contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    }
                }?.asImageBitmap()
            } catch (e: Exception) {
                val stream = contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(stream)?.asImageBitmap()
            }
        }
    }
}
