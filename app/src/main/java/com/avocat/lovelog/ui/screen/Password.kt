package com.avocat.lovelog.ui.screen

import android.content.SharedPreferences
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.avocat.lovelog.Utils
import com.avocat.lovelog.ui.theme.LAccent
import com.avocat.lovelog.ui.theme.LFore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PasswordScreen(
    navController: NavController,
    preferences: SharedPreferences,
    isSet: Boolean = false,
    isRemember: Boolean = false
) {
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    val oldPassword = preferences.getString(Utils.PASSWORD, "")
    val pass2rem = preferences.getString(Utils.PASSWORD2REM, "")
    var password by remember { mutableStateOf("") }
    val passLength = 4

    val coroutineScope = rememberCoroutineScope()

    // Error
    var error by remember { mutableStateOf("") }
    val errorTextAlpha by animateFloatAsState(if (error.isEmpty()) 0f else 1f) {
        if (it != 0f) {
            password = ""
        }
    }

    LaunchedEffect(key1 = true) {
        delay(1000)
        alpha.animateTo(1.5f, tween(500, 0, EaseOut))
    }
    LaunchedEffect(key1 = true) {
        delay(500)
        offsetY.animateTo(-256f, tween(500, easing = EaseOutBack))
    }

    Surface(
        Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            Modifier.offset(0.dp, offsetY.value.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Outlined.Shield,
                "shield",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.scale(3f)
            )
            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (i in 1..passLength) {
                    if (i <= password.length) {
                        Box(
                            Modifier
                                .background(
                                    MaterialTheme.colorScheme.onBackground, CircleShape
                                )
                                .size(24.dp)
                        )
                    } else {
                        Box(
                            Modifier
                                .border(
                                    2.dp, MaterialTheme.colorScheme.onBackground, CircleShape
                                )
                                .size(24.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                error,
                modifier = Modifier.alpha(errorTextAlpha),
                color = LAccent,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Box(
            Modifier.alpha(alpha.value),
            contentAlignment = Alignment.Center
        ) {
            val buttons = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.offset(0.dp, 64.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // numbers
                items(buttons) {
                    IconButton(onClick = {
                        if (passLength > password.length) {
                            password += it
                            error = ""
                        }
                    }) {
                        Text(it)
                    }
                }
                // Remove last char
                item {
                    IconButton(onClick = {
                        if (password.isNotEmpty()) {
                            password = password.substring(0, password.lastIndex)
                            error = ""
                        }
                    }) {
                        Icon(Icons.Outlined.Backspace, "remove")
                    }
                }
                // zero
                item {
                    IconButton(onClick = {
                        if (passLength > password.length) {
                            password += "0"
                            error = ""
                        }
                    }) {
                        Text("0")
                    }
                }
                // Complete
                item {
                    IconButton(onClick = {
                        if (isSet) {
                            navController.popBackStack()
                            navController.navigate("passwordRememberScreen")
                            preferences.edit().putString(Utils.PASSWORD2REM, password).apply()
                        } else if (isRemember) {
                            if (pass2rem != password) {
                                error = "passwords don't match"

                            } else {
                                preferences.edit().putString(Utils.PASSWORD2REM, null).apply()
                                preferences.edit().putString(Utils.PASSWORD, password).apply()
                                navController.navigateUp()
                            }
                        } else if (oldPassword != password) {
                            error = "password isn't correct"
                        } else {
                            navController.popBackStack()
                            navController.navigate("mainScreen")
                        }
                    }) {
                        Icon(Icons.Outlined.Done, "remove")
                    }
                }
            }
        }
    }
}
