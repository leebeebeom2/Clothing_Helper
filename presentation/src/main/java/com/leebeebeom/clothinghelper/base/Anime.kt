package com.leebeebeom.clothinghelper.base

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment

object Anime {
    object Error {
        val errorIn = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium
            ), expandFrom = Alignment.Bottom
        )

        val errorOut = shrinkVertically(
            animationSpec = tween(durationMillis = 0), shrinkTowards = Alignment.Top
        )
    }

    object List {
        val listExpand = expandVertically()
        val listShrink = shrinkVertically()
    }

    object BottomAppbar {
        const val duration = 250
        const val delay = 150

        val expandIn =
            expandVertically(expandFrom = Alignment.Top, animationSpec = tween(duration, delay))
        val shrinkOut =
            shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(duration))
    }

    object SubCategoryCard {
        val expandIn = expandVertically()
        val shrinkOut = shrinkVertically()
    }

    object AddSubCategoryFab {
        val fadeOut = fadeOut(tween(BottomAppbar.duration))
        val fadeIn = fadeIn(tween(BottomAppbar.duration, BottomAppbar.delay))
    }

    @OptIn(ExperimentalAnimationApi::class)
    object CircleCheckBox {
        val expandIn = expandHorizontally() + scaleIn()
        val shrinkOut = shrinkHorizontally() + scaleOut()
    }
}