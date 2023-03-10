package com.avocat.lovelog.ui.screen

import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowRightAlt
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.avocat.lovelog.EventData
import com.avocat.lovelog.Events
import com.avocat.lovelog.R
import com.avocat.lovelog.Utils
import com.avocat.lovelog.ui.composable.Avatar
import com.avocat.lovelog.ui.theme.LAccent
import com.avocat.lovelog.ui.theme.UpRoundedCornerShape24
import java.util.*
import kotlin.math.abs


@Stable
@Composable
fun MainScreen(navController: NavController, preferences: SharedPreferences) {
    val date = preferences.getString(Utils.COUPLE_DATE, "")

    // years, months, days, allDays
    val (_, _, _, allDays) = Utils.getPeriod(date!!)

    val datesNum = LocalContext.current.resources.getIntArray(R.array.dates_num)
    val dates = LocalContext.current.resources.getStringArray(R.array.dates)
    val events by remember { mutableStateOf(Events()) }
    for (i in dates.indices)
        events.addEvent(EventData(dates[i], datesNum[i]))
    // Visible item calculating
    val lazyState = rememberLazyListState(events.lastCompletedIndex(allDays.toInt()))

    val lastDate = events.lastDate(allDays.toInt())
    val nextDate = events.nextDate(allDays.toInt())

    val (leftPartner, rightPartner) = Utils.getCouple(preferences)
    val (leftPhoto, rightPhoto) = Utils.getCouplePhotos(preferences)

    // Background
    Surface(
        Modifier.fillMaxSize(),
        shape = UpRoundedCornerShape24,
        color = MaterialTheme.colorScheme.background
    ) { }
    Column(
        Modifier.offset(0.dp, 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                Modifier.offset(0.dp, 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
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
                    allDays.toString() + LocalContext.current.getString(R.string.day_together),
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
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                val progress = when {
                    nextDate == null -> 1f
                    nextDate.daysCount - allDays == 0L -> 0f
                    else -> (nextDate.daysCount - allDays).toFloat() /
                            (nextDate.daysCount - lastDate.daysCount).toFloat()
                }
                CircularProgressIndicator(progress, Modifier.size(46.dp))
                Text(
                    "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(lastDate.title, style = MaterialTheme.typography.bodyMedium)
            nextDate?.let {
                Icon(
                    Icons.Outlined.ArrowRightAlt,
                    "arrow",
                    tint = MaterialTheme.colorScheme.inversePrimary
                )
                Text(nextDate.title, style = MaterialTheme.typography.bodyMedium)
            }
        }

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
                items(events.list()) {
                    ProgressCard(
                        complete = allDays >= it.daysCount,
                        eventData = it,
                        currentDays = allDays.toInt(),
                        coupleDate = Utils.dateFormat.parse(date)!!
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


@Stable
@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    complete: Boolean = false,
    eventData: EventData = EventData("", 0),
    currentDays: Int = 0,
    coupleDate: Date,
) {
    val ctx = LocalContext.current
    Surface(
        modifier.padding(12.dp, 4.dp),
        color =
            if (complete)
                MaterialTheme.colorScheme.inverseSurface
            else
                MaterialTheme.colorScheme.inversePrimary,
        shape = MaterialTheme.shapes.medium
    ) {
        val daysDiff = abs(currentDays - eventData.daysCount)
        val eventDate = Date(
            abs(eventData.daysCount.toLong() * 24 * 60 * 60 * 1000) + coupleDate.time
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
                    Utils.dateFormat.format(eventDate),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // days/months before
            if (!complete) {
                Box(Modifier.matchParentSize(), contentAlignment = Alignment.BottomEnd) {
                    Text(
                        when {
                            daysDiff <= 30 -> ctx.getString(R.string.days_left) + " $daysDiff"
                            daysDiff <= 365 -> ctx.getString(R.string.months_left) + " ${daysDiff / 30}"
                            else -> ctx.getString(R.string.years_left) + " ${daysDiff / 365}"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
