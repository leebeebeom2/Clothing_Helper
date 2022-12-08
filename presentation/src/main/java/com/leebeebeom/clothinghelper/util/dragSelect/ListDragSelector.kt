package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback

class ListDragSelector(
    private val state: LazyListState,
    haptic: HapticFeedback
) : BaseDragSelector<LazyListItem>(haptic) {

    override val visibleItemsInfo: List<LazyListItem>
        get() = state.layoutInfo.visibleItemsInfo.map { it.toLazyListItem() }

    override fun selectedItemPredicate(
        touchOffset: Offset,
        visibleItem: LazyListItem
    ) = touchOffset.y.toInt() in visibleItem.top..visibleItem.bottom
}