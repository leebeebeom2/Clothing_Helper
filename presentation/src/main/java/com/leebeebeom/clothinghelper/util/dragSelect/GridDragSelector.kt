package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback

class GridDragSelector(
    private val state: LazyGridState,
    haptic: HapticFeedback
) : BaseDragSelector<LazyGridItemInfo>(haptic) {
    private var initialSelectedHeight: Pair<Int, Int>? = null
    private var initialSelectedWidth: Pair<Int, Int>? = null
    private var lastSelectedHeight: Pair<Int, Int>? = null
    private var lastSelectedWidth: Pair<Int, Int>? = null

    override fun getSelectedItem(touchOffset: Offset, task: (LazyGridItemInfo) -> Unit) {
        state.layoutInfo.visibleItemsInfo.firstOrNull {
            println("터치 x: ${touchOffset.x.toInt()}, start: ${it.start}, end: ${it.end}")
            println("터치 y: ${touchOffset.y.toInt()}, top: ${it.top}, bottom: ${it.bottom}")
            touchOffset.y.toInt() in it.top..it.bottom &&
                    touchOffset.x.toInt() in it.start..it.end
        }?.let { task(it) }
    }

    override fun getKey(selectedItem: LazyGridItemInfo) = selectedItem.key as? String

    override fun setInitial(selectedItem: LazyGridItemInfo) {
        initialSelectedIndex = selectedItem.index
        initialSelectedHeight = selectedItem.top to selectedItem.bottom
        initialSelectedWidth = selectedItem.start to selectedItem.end
    }

    override fun setLast(selectedItem: LazyGridItemInfo) {
        lastSelectedIndex = selectedItem.index
        lastSelectedHeight = selectedItem.top to selectedItem.bottom
        lastSelectedWidth = selectedItem.start to selectedItem.end
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
        initialSelectedHeight?.let { height ->
            initialSelectedWidth?.let { width ->
                if (touchOffset.y.toInt() < height.first || touchOffset.x.toInt() < width.first) DragDirection.Up
                else if (touchOffset.y.toInt() > height.second || touchOffset.x.toInt() > width.second) DragDirection.Down
                else DragDirection.None
            }
        }

    override fun getDragEndDirection(touchOffset: Offset): DragDirection {
        val dragEndDirection = lastSelectedHeight?.let { height ->
            lastSelectedWidth?.let { width ->
                if (touchOffset.y.toInt() < height.first || touchOffset.x.toInt() < width.first) DragDirection.Up
                else if (touchOffset.y.toInt() > height.second || touchOffset.x.toInt() > width.second) DragDirection.Down
                else DragDirection.None
            }
        }

        return if ((currentDragDirection == DragDirection.Down || currentDragDirection == DragDirection.None) && dragEndDirection == DragDirection.Up) DragDirection.Up
        else if ((currentDragDirection == DragDirection.Up || currentDragDirection == DragDirection.None) && dragEndDirection == DragDirection.Down) DragDirection.Down
        else DragDirection.None
    }

    override fun dragEnd() {
        initialSelectedHeight = null
        initialSelectedWidth = null
        lastSelectedHeight = null
        lastSelectedWidth = null
    }
}

val LazyGridItemInfo.top get() = offset.y + 23
val LazyGridItemInfo.bottom get() = offset.y + size.height - 58
val LazyGridItemInfo.start get() = offset.x + 90
val LazyGridItemInfo.end get() = offset.x + size.width - 10