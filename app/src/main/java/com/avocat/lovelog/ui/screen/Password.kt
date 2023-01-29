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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.avocat.lovelog.R
import com.avocat.lovelog.Utils
import com.avocat.lovelog.ui.theme.LAccent
import kotlinx.coroutines.delay


@Composable
fun PasswordScreen(
    navController: NavController,
    preferences: SharedPreferences,
    isSet: Boolean = false,
    isRemember: Boolean = false,
    isClear: Boolean = false,
    isReset: Boolean = false,
    passwordLength: Int = 4
) {
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    val oldPassword = preferences.getString(Utils.PASSWORD, "")
    val pass2rem = preferences.getString(Utils.PASSWORD2REM, "")
    var password by remember { mutableStateOf("") }

    val ctx = LocalContext.current

    // Error
    var error by remember { mutableStateOf("") }
    val errorTextAlpha by animateFloatAsState(if (error.isEmpty()) 0f else 1f) {
        if (it != 0f) {
            password = ""
        }
    }

    val circleModifier = Modifier.size(24.dp)
    val borderCircleModifier = circleModifier.border(
        2.dp, MaterialTheme.colorScheme.onBackground, CircleShape
    )
    val backCircleModifier = circleModifier.background(
        MaterialTheme.colorScheme.onBackground, CircleShape
    )

    LaunchedEffect(key1 = true) {
        delay(1000)
        alpha.animateTo(1.5f, tween(500, 0, EaseOut))
    }
    LaunchedEffect(key1 = true) {
        delay(500)
        offsetY.animateTo(-200f, tween(500, easing = EaseOutBack))
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
                for (i in 1..passwordLength) {
                    if (i <= password.length) {
                        Box(backCircleModifier)
                    } else {
                        Box(borderCircleModifier)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            Text(
                when {
                    isClear || isReset -> LocalContext.current.getString(R.string.enter_password)
                    isRemember -> LocalContext.current.getString(R.string.repeat_pass)
                    isSet -> LocalContext.current.getString(R.string.set_pass_text)
                    else -> LocalContext.current.getString(R.string.enter_password)
                },
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.offset(0.dp, 64.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // numbers
                items(('1'..'9').toList()) {
                    IconButton(onClick = {
                        if (passwordLength > password.length) {
                            password += it
                            error = ""
                        }
                    }) {
                        Text(it.toString())
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
                        if (passwordLength > password.length) {
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
                            // Set a new password
                            navController.popBackStack()
                            navController.navigate("passwordScreen?isRemember=true")
                            preferences.edit().putString(Utils.PASSWORD2REM, password).apply()
                        } else if (isRemember) {
                            // Repeat the new password
                            if (pass2rem != password) {
                                error = ctx.getString(R.string.pass_dont_match)
                            } else {
                                preferences.edit()
                                    .putString(Utils.PASSWORD2REM, null)
                                    .putString(Utils.PASSWORD, password)
                                    .apply()
                                navController.navigateUp()
                            }
                        } else if (isClear) {
                            // Clear old password
                            if (oldPassword != password)
                                error = ctx.getString(R.string.wrong_pass)
                            preferences.edit()
                                .putString(Utils.PASSWORD2REM, null)
                                .putString(Utils.PASSWORD, null)
                                .apply()
                            navController.navigateUp()
                        } else if (isReset) {
                            // Set a new password (works when password isn't empty)
                            if (oldPassword != password) {
                                error = ctx.getString(R.string.wrong_pass)
                            } else {
                                // Valid password
                                navController.popBackStack()
                                navController.navigate("passwordScreen?isSet=true")
                            }
                        } else if (oldPassword != password) {
                            // Incorrect password
                            error = ctx.getString(R.string.wrong_pass)
                        } else {
                            // Valid password
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
