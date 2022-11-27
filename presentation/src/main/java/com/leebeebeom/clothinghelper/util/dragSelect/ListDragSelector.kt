package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback

class ListDragSelector(
    private val state: LazyListState,
    haptic: HapticFeedback
) : BaseDragSelector<ListSelectedItem>(haptic) {

    override fun getSelectedItem(touchOffset: Offset, task: (ListSelectedItem) -> Unit) {
        state.layoutInfo.visibleItemsInfo.firstOrNull { visibleItem ->
            (visibleItem.key as? String)?.let {
                touchOffset.y.toInt() in visibleItem.top..visibleItem.bottom
            } ?: false
        }?.let { selectedItem ->
            task(selectedItem.toListSelectedItem())
        }
    }

    override fun getSelectedItems(
        selectedItem: ListSelectedItem,
        indexRange: IntRange,
        passedItemsContainsOrNot: (ListSelectedItem) -> Boolean
    ) = state.layoutInfo.visibleItemsInfo.filter { visibleItem ->
        (visibleItem.key as? String)?.let { _ ->
            visibleItem.index in indexRange && passedItemsContainsOrNot(visibleItem.toListSelectedItem())
        } ?: false
    }.map { it.toListSelectedItem() }.toSet()
}