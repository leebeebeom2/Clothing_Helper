package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback

class ListDragSelector(
    private val state: LazyListState,
    haptic: HapticFeedback
) : BaseDragSelector<ListSelectedItem>(haptic) {

    override val visibleItemsInfo: List<ListSelectedItem>
        get() = state.layoutInfo.visibleItemsInfo.map { it.toListSelectedItem() }

    override fun selectedItemPredicate(
        touchOffset: Offset,
        visibleItem: ListSelectedItem
    ) = touchOffset.y.toInt() in visibleItem.top..visibleItem.bottom
}