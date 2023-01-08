package com.avocat.lovelog.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    surface = DBg,
    onSurface = DFore,
    surfaceTint = DBg,
    surfaceVariant = DBg,
    inverseSurface = DFore,
    inverseOnSurface = DBg,
    inversePrimary = DAccent,
    primary = DPrimary,
    primaryContainer = DBg,
    onPrimary = DBg,
    onSecondary = DBg,
    secondary = DAccent,
    tertiary = DPrimary,
    background = DBg,
    onBackground = DFore
)

private val LightColorScheme = lightColorScheme(
    surface = LBg,
    onSurface = LFore,
    surfaceTint = LBg,
    surfaceVariant = LBg,
    inverseSurface = LFore,
    inverseOnSurface = LBg,
    inversePrimary = LAccent,
    primary = LPrimary,
    primaryContainer = LBg,
    onPrimary = LBg,
    onSecondary = LBg,
    secondary = LAccent,
    tertiary = LPrimary,
    background = LBg,
    onBackground = LFore
)

@Composable
fun LoveLogTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colors = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}