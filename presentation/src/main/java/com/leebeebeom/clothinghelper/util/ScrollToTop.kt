package com.leebeebeom.clothinghelper.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.derivedStateOf

private val showScrollToTopFab: (firstVisibleItemScrollOffset: Int) -> Boolean = {
    derivedStateOf { it > 0 }.value
}

val LazyListState.showScrollToTopFab
    get() = showScrollToTopFab(firstVisibleItemIndex)

val LazyGridState.showScrollToTopFab
    get() = showScrollToTopFab(firstVisibleItemIndex)

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