package com.avocat.lovelog.ui.screen

import android.content.SharedPreferences
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
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
    val scale = remember { Animatable(.2f) }
    val offsetY = remember { Animatable(256f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(.5f, tween(500, easing = EaseOutBack, delayMillis = 300))
        delay(500)
        scale.animateTo(.2f, tween(500, easing = EaseInBack))
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

    LaunchedEffect(key1 = true) {
        offsetY.animateTo(0f, tween(500, easing = EaseOutBack, delayMillis = 300))
        delay(500)
        offsetY.animateTo(-256f, tween(500, easing = EaseInBack))
    }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(1f, tween(500, easing = EaseOutBack, delayMillis = 300))
        delay(500)
        alpha.animateTo(0f, tween(500, easing = EaseInBack))
    }

    Box(
        Modifier.fillMaxSize().offset(0.dp, offsetY.value.dp).scale(scale.value).alpha(alpha.value),
        contentAlignment = Alignment.Center
    ) {
        Image(ImageVector.vectorResource(id = R.drawable.ic_loveloglighticon), null)
    }
}
