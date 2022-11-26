package com.leebeebeom.clothinghelper.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf

val LazyListState.showScrollToTopFab
    get() = derivedStateOf { firstVisibleItemScrollOffset > 0 }.value

suspend fun LazyListState.scrollToTop() {
    val scrollToTop = suspend { animateScrollToItem(0) }

    if (firstVisibleItemIndex <= 2) scrollToTop()
    else {
        scrollToItem(2)
        scrollToTop()
    }
}