package com.avocat.lovelog.ui.screen

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.avocat.lovelog.EventData
import com.avocat.lovelog.R
import com.avocat.lovelog.Utils
import com.avocat.lovelog.ui.composable.Avatar
import com.avocat.lovelog.ui.icon.Heart
import com.avocat.lovelog.ui.theme.LAccent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


@Composable
fun MainScreen(navController: NavController, preferences: SharedPreferences) {
    val date = preferences.getString(Utils.COUPLE_DATE, "")

    // Animations
    val offsetY = remember { Animatable(256f) }
    val alpha = remember { Animatable(0f) }
    val modifierAlpha = Modifier.alpha(alpha.value)

    LaunchedEffect(key1 = true) {
        offsetY.animateTo(0f, tween(500, 300, EaseOutBack))
    }
    LaunchedEffect(key1 = true) {
        alpha.animateTo(1f, tween(300, 300, EaseOutBack))
    }

    val (years, months, days, allDays) = Utils.getPeriod(date!!)
    println(years)
    println(months)
    println(days)

    val i = listOf(
        EventData("100 days", 100),
        EventData("200 days", 200),
        EventData("300 days", 300),
        EventData("1 year", 365),
        EventData("400 days", 400),
        EventData("500 days", 500),
        EventData("600 days", 600),
        EventData("2 years", 730),
        EventData("1000 days", 1000),
        EventData("3 years", 1095),
        EventData("4 years", 1460),
        EventData("5 years", 1825),
        EventData("6 years",  2190),
    )
    // Visible item calculating
    var nextIndex = 0
    while (nextIndex < i.size-1 && i[nextIndex+1].daysCount <= allDays) {
        nextIndex++
    }
    val lazyState = rememberLazyListState(nextIndex)

    val (leftPartner, rightPartner) = Utils.getCouple(preferences)
    val (leftPhoto, rightPhoto) = Utils.getCouplePhotos(preferences)

    // Main content
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        // Gradient
        Box(
            Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFFFF9EF5), Color(0xFF8C9FFF))
                    )
                )
                .fillMaxWidth()
        )
        // Background
        Surface(
            modifierAlpha
                .fillMaxSize()
                .offset(0.dp, (80f + offsetY.value).dp),
            shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
            color = MaterialTheme.colorScheme.background
        ) { }
        Column(
            modifierAlpha
                .offset(0.dp, (36f + offsetY.value).dp)
        ) {

            // Partners and main info
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Avatar(
                        imageBitmap = Utils.uriToImageBitmap(
                            Uri.parse(leftPhoto), LocalContext.current.contentResolver
                        )
                    )
                    Text(leftPartner, style = MaterialTheme.typography.bodyMedium)
                }
                Column(
                    modifierAlpha
                        .offset(0.dp, 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                        Image(
                            Icons.Outlined.Heart,
                            "heart",
                            Modifier.height(40.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background),
                            contentScale = ContentScale.FillHeight
                        )
                        Image(
                            Icons.Outlined.Favorite,
                            "heart",
                            Modifier.height(40.dp),
                            colorFilter = ColorFilter.tint(LAccent),
                            contentScale = ContentScale.FillHeight
                        )
                    }
                    Text(date, style = MaterialTheme.typography.bodySmall)
                    Text(
                        allDays.toString() + " " + LocalContext.current.getString(R.string.day_together),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Avatar(
                        imageBitmap = Utils.uriToImageBitmap(
                            Uri.parse(rightPhoto),
                            LocalContext.current.contentResolver
                        )
                    )
                    Text(rightPartner, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Progress

            // Cards
            Column(
                Modifier
                    .fillMaxSize()
                    .offset(0.dp, (-48).dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyRow(
                    state = lazyState,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(i) {
                        ProgressCard(
                            complete = allDays >= it.daysCount,
                            eventData = it,
                            currentDays = allDays.toInt(),
                            coupleDate = date
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate("settingsScreen")
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Settings,
                            "settings",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}


@SuppressLint("SimpleDateFormat")
@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    complete: Boolean = false,
    eventData: EventData = EventData("", 0),
    currentDays: Int = 0,
    coupleDate: String,
) {
    Surface(
        modifier.padding(12.dp, 4.dp),
        color =
            if (complete)
                MaterialTheme.colorScheme.inverseSurface
            else
                MaterialTheme.colorScheme.inversePrimary,
        shape = MaterialTheme.shapes.medium
    ) {
        val date = SimpleDateFormat("dd.MM.yyyy").parse(coupleDate)!!
        val daysDiff = abs(currentDays - eventData.daysCount)
        val eventDate = Date(
            abs(eventData.daysCount.toLong() * 24 * 60 * 60 * 1000) + date.time
        )

        Box(
            Modifier
                .wrapContentSize()
                .padding(8.dp, 4.dp),
            contentAlignment = Alignment.Center
        ) {
            // event title
            Text(eventData.title, modifier = Modifier.padding(48.dp, 36.dp))

            // event date
            Box(Modifier.matchParentSize(), contentAlignment = Alignment.BottomStart) {
                Text(
                    SimpleDateFormat("dd.MM.yyyy").format(eventDate),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // days/months before
            if (!complete) {
                val ctx = LocalContext.current
                val text = when {
                    daysDiff <= 30 -> ctx.getString(R.string.days_left) + " $daysDiff"
                    daysDiff <= 365 -> ctx.getString(R.string.months_left) + " ${daysDiff / 30}"
                    else -> ctx.getString(R.string.years_left) + " ${daysDiff / 365}"
                }
                Box(Modifier.matchParentSize(), contentAlignment = Alignment.BottomEnd) {
                    Text(text, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
