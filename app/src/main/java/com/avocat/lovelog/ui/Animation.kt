package com.avocat.lovelog.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visibleState = remember {
            MutableTransitionState(false)
        }.apply { targetState = true },
        modifier = Modifier,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        content()
    }
}


@Composable
fun SlideUpAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visibleState = remember {
            MutableTransitionState(false)
        }.apply { targetState = true },
        enter = slideInVertically { it/2 } + fadeIn(),
        exit = slideOutVertically { -it/2 } + fadeOut()
    ) {
        content()
    }
}