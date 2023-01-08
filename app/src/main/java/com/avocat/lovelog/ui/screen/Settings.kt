package com.avocat.lovelog.ui.screen

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.avocat.lovelog.R


@Composable
fun SettingsScreen(navController: NavController, preferences: SharedPreferences) {
    Surface(
        Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.large
    ) {
        Box(Modifier.padding(8.dp)) {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    "back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                LocalContext.current.getString(R.string.settings),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(2.dp, 4.dp)
            )
            Divider(Modifier.width(128.dp))
            Spacer(Modifier.height(24.dp))
            Button(onClick = {
                navController.navigate("passwordSetScreen")
            }) {
                Text("set password")
            }
        }
    }
}
