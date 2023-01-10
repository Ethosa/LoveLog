package com.avocat.lovelog

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            val ctx = LocalContext.current
            val permissionGranted = remember { mutableStateOf(
                ContextCompat.checkSelfPermission(
                    ctx, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) }

            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                permissionGranted.value = it
            }

            SideEffect {
                if (!permissionGranted.value)
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

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
                        composable(
                            "passwordScreen?isSet={set}&isRemember={rem}&isClear={clear}&isReset={reset}",
                            arguments = listOf(
                                navArgument("set") {
                                    defaultValue = false
                                    type = NavType.BoolType
                                },
                                navArgument("rem") {
                                    defaultValue = false
                                    type = NavType.BoolType
                                },
                                navArgument("clear") {
                                    defaultValue = false
                                    type = NavType.BoolType
                                },
                                navArgument("reset") {
                                    defaultValue = false
                                    type = NavType.BoolType
                                },
                            )
                        ) {
                            EnterAnimation {
                                PasswordScreen(
                                    navController,
                                    sharedPreferences,
                                    isSet = it.arguments?.getBoolean("set")!!,
                                    isClear = it.arguments?.getBoolean("clear")!!,
                                    isRemember = it.arguments?.getBoolean("rem")!!,
                                    isReset = it.arguments?.getBoolean("reset")!!,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
