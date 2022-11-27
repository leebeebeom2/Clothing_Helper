package com.leebeebeom.clothinghelper.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.derivedStateOf

val LazyListState.showScrollToTopFab
    get() = derivedStateOf { firstVisibleItemScrollOffset > 0 }.value

val LazyGridState.showScrollToTopFab
    get() = derivedStateOf { firstVisibleItemScrollOffset > 0 }.value

suspend fun LazyListState.scrollToTop() {
    val scrollToTop = suspend { animateScrollToItem(0) }

    if (firstVisibleItemIndex <= 2) scrollToTop()
    else {
        scrollToItem(2)
        scrollToTop()
    }
}

suspend fun LazyGridState.scrollToTop() {
    val scrollToTop = suspend { animateScrollToItem(0) }

    if (firstVisibleItemIndex <= 5) scrollToTop()
    else {
        scrollToItem(5)
        scrollToTop()
    }
}