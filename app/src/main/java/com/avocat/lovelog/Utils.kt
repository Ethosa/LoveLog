package com.avocat.lovelog

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs


@Stable
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
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")

        /**
         * Loads saved dating date if available
         */
        fun getDate(preferences: SharedPreferences): Date? {
            val date = preferences.getString(COUPLE_DATE, null)
            var result: Date? = null
            date?.let {
                result = dateFormat.parse(date)!!
            }
            return result
        }

        /**
         * Loads saved partners names
         */
        fun getCouple(preferences: SharedPreferences): List<String> {
            return listOf(
                preferences.getString(LEFT_PARTNER, "")!!,
                preferences.getString(RIGHT_PARTNER, "")!!
            )
        }

        /**
         * Loads saved partners photos
         */
        fun getCouplePhotos(preferences: SharedPreferences): List<String> {
            return listOf(
                preferences.getString(LEFT_AVATAR, "")!!,
                preferences.getString(RIGHT_AVATAR, "")!!
            )
        }

        /**
         * Calculates period between specified date and now date (without leap years)
         * @param date - specified date
         */
        fun getPeriod(date: String): List<Long> {
            val start: Date = dateFormat.parse(date)!!
            val end = Date()
            val diff = abs(end.time - start.time)
            val days = diff / 1000 / 60 / 60 / 24
            val months = days / 30
            val years = days / 365
            return listOf(years, months % 12, (days % 365) % 30, days)
        }

        /**
         * Loads ImageBitmap from Uri
         */
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
