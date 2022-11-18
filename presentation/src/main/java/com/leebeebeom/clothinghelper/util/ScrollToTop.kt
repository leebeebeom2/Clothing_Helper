package com.leebeebeom.clothinghelper.util

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue

val LazyListState.showScrollToTopButton
    get() = run {
        val show by derivedStateOf { firstVisibleItemScrollOffset > 0 }
        show
    }

suspend fun LazyListState.scrollToTop() {
    val scrollAnimationSpec = tween<Float>(300, easing = FastOutLinearInEasing)
    val firstVisibleItemIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index

    firstVisibleItemIndex?.also {
        if (it <= 2) animateScrollBy(-1000f, scrollAnimationSpec)
        else {
            scrollToItem(2)
            animateScrollBy(-1000f, scrollAnimationSpec)
        }
    }
}