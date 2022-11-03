package com.leebeebeom.clothinghelper.base

import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment

object Anime {
    object Screen {
        private const val screenSlideDuration = 350
        private val screenSlideEasing = CubicBezierEasing(a = 0.36f, b = 0f, c = 0.66f, d = -0.1f)

        val screenSlideInBottom =
            slideInVertically(animationSpec = tween(screenSlideDuration)) { it }

        val screenSlideOutBottom =
            slideOutVertically(
                animationSpec = tween(
                    screenSlideDuration,
                    easing = screenSlideEasing
                )
            ) { it }
    }

    object Error {
        val errorIn = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            expandFrom = Alignment.Bottom
        )

        val errorOut = shrinkVertically(
            animationSpec = tween(durationMillis = 50),
            shrinkTowards = Alignment.Top
        )
    }

    object ListExpand {
        val listExpand = expandVertically()
        val listShrink = shrinkVertically()
    }

    object BottomAppbar {
        const val duration = 250

        val bottomAppbarExpandIn = expandVertically(
            expandFrom = Alignment.Top, animationSpec = tween(durationMillis = duration)
        )

        val bottomAppbarShrinkOut = shrinkVertically(
            shrinkTowards = Alignment.Top, animationSpec = tween(durationMillis = duration)
        )
    }

    object SubCategoryCard {
        private const val duration = 250

        val cardExpandIn = expandVertically(animationSpec = tween(durationMillis = duration))
        val cardShrinkOut = shrinkVertically(animationSpec = tween(durationMillis = duration))
    }

    @OptIn(ExperimentalAnimationApi::class)
    object CircleCheckBox{
        const val duration = 300

        val checkBoxIn = expandHorizontally(tween(duration)) { 0 } + scaleIn(tween(duration))
        val checkBoxOut = shrinkHorizontally(tween(duration)) { 0 } + scaleOut(tween(duration))
    }
}