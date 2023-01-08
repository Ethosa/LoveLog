package com.avocat.lovelog.ui.screen

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.avocat.lovelog.R
import com.avocat.lovelog.Utils
import com.avocat.lovelog.ui.composable.DatePickerField


@Composable
fun FirstStepScreen(navController: NavController, preferences: SharedPreferences) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)+1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var date by remember { mutableStateOf("$day.$month.$year") }

    val picker = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, y: Int, m: Int, d: Int -> date = "$d.${m+1}.$y" },
        year, month, day
    )

    // Animations
    val alpha = remember { Animatable(0f) }
    val offsetTitleY = remember { Animatable(32f) }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(1f, tween(1000, easing = EaseOut))
    }
    LaunchedEffect(key1 = true) {
        offsetTitleY.animateTo(0f, tween(1000, 300, EaseOut))
    }

    Box(Modifier.fillMaxSize().alpha(alpha.value), contentAlignment = Alignment.Center) {
        Text(
            LocalContext.current.getString(R.string.start_dating),
            Modifier.offset(0.dp, (-48 - offsetTitleY.value).dp)
        )
        DatePickerField(onEdit = {
            date = it
        }) {
            date = it
            preferences.edit().putString(Utils.COUPLE_DATE, it).apply()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(12.dp), contentAlignment = Alignment.BottomEnd) {
        Button(onClick = {
            preferences.edit().putString(Utils.COUPLE_DATE, date).apply()
            navController.popBackStack()
            navController.navigate("mainScreen")
        }) {
            Text(LocalContext.current.getString(R.string.contn))
        }
    }
}