package com.leebeebeom.clothinghelper.base

import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment

object Anime {
    object Screen {
        private const val duration = 350
        private val easing = CubicBezierEasing(a = 0.36f, b = 0f, c = 0.66f, d = -0.1f)

        val slideInBottom =
            slideInVertically(animationSpec = tween(duration)) { it }

        val slideOutBottom = slideOutVertically(
            animationSpec = tween(
                duration, easing = easing
            )
        ) { it }
    }

    object Error {
        val errorIn = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium
            ), expandFrom = Alignment.Bottom
        )

        val errorOut = shrinkVertically(
            animationSpec = tween(durationMillis = 50), shrinkTowards = Alignment.Top
        )
    }

    object List {
        val listExpand = expandVertically()
        val listShrink = shrinkVertically()
    }

    object BottomAppbar {
        const val duration = 250

        val expandIn = expandVertically(
            expandFrom = Alignment.Top, animationSpec = tween(durationMillis = duration)
        )

        val shrinkOut = shrinkVertically(
            shrinkTowards = Alignment.Top, animationSpec = tween(durationMillis = duration)
        )
    }

    object SubCategoryCard {
        private const val duration = 250

        val expandIn = expandVertically(animationSpec = tween(durationMillis = duration))
        val shrinkOut = shrinkVertically(animationSpec = tween(durationMillis = duration))
    }

    @OptIn(ExperimentalAnimationApi::class)
    object CircleCheckBox {
        const val duration = 300

        val expandIn = expandHorizontally(tween(duration)) { 0 } + scaleIn(tween(duration))
        val shrinkOut = shrinkHorizontally(tween(duration)) { 0 } + scaleOut(tween(duration))
    }
}