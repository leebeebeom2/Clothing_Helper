package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

class GridDragSelector(
    private val state: LazyGridState,
    haptic: HapticFeedback
) : BaseDragSelector<LazyGridItemInfo>(haptic) {
    private var initialSelectedOffset: IntOffset? = null
    private var initialSelectedSize: IntSize? = null
    private var lastSelectedOffset: IntOffset? = null
    private var lastSelectedSize: IntSize? = null

    override fun getSelectedItem(touchOffset: Offset, task: (LazyGridItemInfo) -> Unit) {
        state.layoutInfo.visibleItemsInfo.firstOrNull {
            touchOffset.y.toInt() in it.offset.y..(it.offset.y + it.size.height) && // TODO 보정
                    touchOffset.x.toInt() in it.offset.x..(it.offset.x + it.size.width) // TODO 보정
        }?.let { task(it) }
    }

    override fun getKey(selectedItem: LazyGridItemInfo) = selectedItem.key as? String

    override fun setInitial(selectedItem: LazyGridItemInfo) {
        initialSelectedIndex = selectedItem.index
        initialSelectedOffset = selectedItem.offset
        initialSelectedSize = selectedItem.size
    }

    override fun setLast(selectedItem: LazyGridItemInfo) {
        lastSelectedIndex = selectedItem.index
        lastSelectedOffset = selectedItem.offset
        lastSelectedSize = selectedItem.size
    }

    override fun getSelectedItems(
        selectedItem: LazyGridItemInfo,
        indexRange: (selectedItemIndex: Int) -> IntRange,
        passedItemKeysContainsOrNot: (String) -> Boolean
    ) = state.layoutInfo.visibleItemsInfo.filter {
        (it.key as? String)?.let { key ->
            it.index != initialSelectedIndex &&
                    it.index in indexRange(selectedItem.index) &&
                    passedItemKeysContainsOrNot(key)
        } ?: false
    }

    override fun getSelectedKeys(selectedItems: List<LazyGridItemInfo>) =
        selectedItems.map { it.key as String }

    override fun getDragDirection(touchOffset: Offset): DragDirection? =
        initialSelectedOffset?.let { offset ->
            initialSelectedSize?.let { size ->
                if (offset.y > touchOffset.y.toInt() || offset.x > touchOffset.x) DragDirection.Up
                else if (size.height < touchOffset.y.toInt() || size.width < touchOffset.x) DragDirection.Down
                else DragDirection.None
            }
        }

    override fun getDragEndDirection(touchOffset: Offset): DragDirection {
        val dragEndDirection = lastSelectedOffset?.let { offset ->
            lastSelectedSize?.let { size ->
                if (offset.y > touchOffset.y.toInt() || offset.x > touchOffset.x) DragDirection.Up
                else if (size.height < touchOffset.y.toInt() || size.width < touchOffset.x) DragDirection.Down
                else DragDirection.None
            }
        }

        return if ((dragDirection == DragDirection.Down || dragDirection == DragDirection.None) && dragEndDirection == DragDirection.Up) DragDirection.Up
        else if ((dragDirection == DragDirection.Up || dragDirection == DragDirection.None) && dragEndDirection == DragDirection.Down) DragDirection.Down
        else DragDirection.None
    }

    override fun dragEnd() {
        initialSelectedOffset = null
        initialSelectedSize = null
        lastSelectedOffset = null
        lastSelectedSize = null
    }
}