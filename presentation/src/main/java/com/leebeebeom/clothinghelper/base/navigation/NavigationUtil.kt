package com.leebeebeom.clothinghelper.base.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)

object NavigateAnime {
    private const val duration = 350
    private val easing = CubicBezierEasing(0.36f, 0f, 0.66f, -0.1f)

    val slideInRight = { _: AnimatedContentScope<*> ->
        slideInHorizontally(tween(duration)) { it }
    }

    val slideOutRight = { _: AnimatedContentScope<*> ->
        slideOutHorizontally(tween(duration, easing = easing)) { it }
    }

    val slideInBottom = { _: AnimatedContentScope<*> ->
        slideInVertically(tween(duration)) { it }
    }

    val slideOutBottom = { _: AnimatedContentScope<*> ->
        slideOutVertically(tween(duration, easing = easing)) { it }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BaseNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    builder: NavGraphBuilder.() -> Unit
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        modifier = modifier,
        builder = builder
    )
}