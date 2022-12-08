package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback

class GridDragSelector(
    private val state: LazyGridState,
    haptic: HapticFeedback
) : BaseDragSelector<LazyGridItem>(haptic) {

    override val visibleItemsInfo: List<LazyGridItem>
        get() = state.layoutInfo.visibleItemsInfo.map { it.toLazyGridItem() }

    override fun selectedItemPredicate(
        touchOffset: Offset,
        visibleItem: LazyGridItem
    ) = touchOffset.y.toInt() in visibleItem.top..visibleItem.bottom &&
            touchOffset.x.toInt() in visibleItem.start..visibleItem.end
}