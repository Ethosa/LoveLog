package com.avocat.lovelog

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avocat.lovelog.ui.EnterAnimation
import com.avocat.lovelog.ui.SlideUpAnimation
import com.avocat.lovelog.ui.screen.*
import com.avocat.lovelog.ui.theme.LoveLogTheme
import java.util.Date


class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    private var coupleDate: Date? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        var startDestination =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val splashScreen = installSplashScreen()
                splashScreen.setKeepOnScreenCondition { true }
                "mainScreen"
            } else {
                "splashScreen"
            }

        sharedPreferences = getSharedPreferences("com.avocat.lovelog", MODE_PRIVATE)
        coupleDate = Utils.getDate(sharedPreferences)

        if (coupleDate == null && startDestination == "mainScreen")
            startDestination = "firstStepScreen"

        super.onCreate(savedInstanceState)

        val password = sharedPreferences.getString(Utils.PASSWORD, null)
        if (password != null && startDestination != "splashScreen") {
            startDestination = "passwordScreen"
        }

        setContent {
            val navController = rememberNavController()
            navController.visibleEntries

            LoveLogTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable("splashScreen") {
                            EnterAnimation {
                                SplashScreen(navController, sharedPreferences)
                            }
                        }
                        composable("firstStepScreen") {
                            EnterAnimation {
                                FirstStepScreen(navController, sharedPreferences)
                            }
                        }
                        composable("mainScreen") {
                            EnterAnimation {
                                MainScreen(navController, sharedPreferences)
                            }
                        }
                        composable("settingsScreen") {
                            SlideUpAnimation {
                                SettingsScreen(navController, sharedPreferences)
                            }
                        }
                        composable("passwordScreen") {
                            EnterAnimation {
                                PasswordScreen(navController, sharedPreferences)
                            }
                        }
                        composable("passwordSetScreen") {
                            EnterAnimation {
                                PasswordScreen(navController, sharedPreferences, isSet = true)
                            }
                        }
                        composable("passwordRememberScreen") {
                            EnterAnimation {
                                PasswordScreen(navController, sharedPreferences, isRemember = true)
                            }
                        }
                    }
                }
            }
        }
    }
}
