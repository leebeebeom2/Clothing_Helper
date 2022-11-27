package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback

class GridDragSelector(
    private val state: LazyGridState,
    haptic: HapticFeedback
) : BaseDragSelector<GridSelectedItem>(haptic) {

    override fun getSelectedItem(touchOffset: Offset, task: (GridSelectedItem) -> Unit) {
        state.layoutInfo.visibleItemsInfo.firstOrNull { visibleItem ->
            (visibleItem.key as? String)?.let {
                touchOffset.y.toInt() in visibleItem.top..visibleItem.bottom &&
                        touchOffset.x.toInt() in visibleItem.start..visibleItem.end
            } ?: false
        }?.let { selectedItem ->
            task(selectedItem.toGridSelectedItem())
        }
    }

    override fun getSelectedItems(
        selectedItem: GridSelectedItem,
        indexRange: IntRange,
        passedItemsContainsOrNot: (GridSelectedItem) -> Boolean
    ) = state.layoutInfo.visibleItemsInfo.filter { visibleItem ->
        (visibleItem.key as? String)?.let { _ ->
            visibleItem.index in indexRange && passedItemsContainsOrNot(visibleItem.toGridSelectedItem())
        } ?: false
    }.map { it.toGridSelectedItem() }.toSet()
}