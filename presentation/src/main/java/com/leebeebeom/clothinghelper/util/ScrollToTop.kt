package com.leebeebeom.clothinghelper.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.derivedStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val showScrollToTopFab: (firstVisibleItemScrollOffset: Int) -> Boolean = {
    derivedStateOf { it > 0 }.value
}

val LazyListState.showScrollToTopFab
    get() = showScrollToTopFab(firstVisibleItemIndex)

val LazyGridState.showScrollToTopFab
    get() = showScrollToTopFab(firstVisibleItemIndex)

fun LazyListState.scrollToTop(scope: CoroutineScope) {
    val scrollToTop = suspend { animateScrollToItem(0) }

    scope.launch {
        if (firstVisibleItemIndex <= 2) scrollToTop()
        else {
            scrollToItem(2)
            scrollToTop()
        }
    }
}

fun LazyGridState.scrollToTop(scope: CoroutineScope) {
    val scrollToTop = suspend { animateScrollToItem(0) }

    scope.launch {
        if (firstVisibleItemIndex <= 5) scrollToTop()
        else {
            scrollToItem(5)
            scrollToTop()
        }
    }
}