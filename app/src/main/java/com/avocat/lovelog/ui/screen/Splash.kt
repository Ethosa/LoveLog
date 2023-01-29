package com.avocat.lovelog.ui.screen

import android.content.SharedPreferences
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.avocat.lovelog.R
import com.avocat.lovelog.Utils
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController, preferences: SharedPreferences) {
    // Animations
    val scale = remember { Animatable(.4f) }
    val offsetY = remember { Animatable(128f) }
    val alpha = remember { Animatable(0f) }

    // Scale
    LaunchedEffect(key1 = true) {
        scale.animateTo(.5f, tween(500, easing = EaseInExpo, delayMillis = 300))
        delay(800)
        scale.animateTo(.4f, tween(500, easing = EaseOutExpo))
    }

    // Alpha
    LaunchedEffect(key1 = true) {
        alpha.animateTo(1f, tween(500, easing = EaseInExpo, delayMillis = 300))
        delay(800)
        alpha.animateTo(0f, tween(500, easing = EaseOutExpo))
    }

    // Offset
    LaunchedEffect(key1 = true) {
        offsetY.animateTo(0f, tween(500, easing = EaseInExpo, delayMillis = 300))
        delay(800)
        offsetY.animateTo(-128f, tween(500, easing = EaseOutExpo))
        delay(500)
        val date = Utils.getDate(preferences)
        val password = preferences.getString(Utils.PASSWORD, null)
        navController.popBackStack()
        when {
            date == null -> navController.navigate("firstStepScreen")
            password != null -> navController.navigate("passwordScreen")
            else -> navController.navigate("mainScreen")
        }
    }

    Box(
        Modifier.fillMaxSize().offset(0.dp, offsetY.value.dp).scale(scale.value).alpha(alpha.value),
        contentAlignment = Alignment.Center
    ) {
        Image(ImageVector.vectorResource(id = R.drawable.ic_loveloglighticon), null)
    }
}
