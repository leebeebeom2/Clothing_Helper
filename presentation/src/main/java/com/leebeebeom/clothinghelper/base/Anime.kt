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
            animationSpec = tween(durationMillis = 50), shrinkTowards = Alignment.Top
        )
    }

    object List {
        val listExpand = expandVertically()
        val listShrink = shrinkVertically()
    }

    object BottomAppbar {
        val expandIn = expandVertically(expandFrom = Alignment.Top)
        val shrinkOut = shrinkVertically(shrinkTowards = Alignment.Top)
    }

    object SubCategoryCard {
        val expandIn = expandVertically()
        val shrinkOut = shrinkVertically()
    }

    @OptIn(ExperimentalAnimationApi::class)
    object CircleCheckBox {
        val expandIn = expandHorizontally() + scaleIn()
        val shrinkOut = shrinkHorizontally() + scaleOut()
    }
}