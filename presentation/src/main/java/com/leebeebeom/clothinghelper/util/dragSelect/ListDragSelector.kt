package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback

class ListDragSelector(
    private val state: LazyListState,
    haptic: HapticFeedback
) : BaseDragSelector<LazyListItemInfo>(haptic) {
    private var initialSelectedTop: Int? = null
    private var initialSelectedBottom: Int? = null
    private var lastSelectedTop: Int? = null
    private var lastSelectedBottom: Int? = null

    override fun getSelectedItem(touchOffset: Offset, task: (LazyListItemInfo) -> Unit) {
        state.layoutInfo.visibleItemsInfo.firstOrNull {
            println("터치: ${touchOffset.y.toInt()}, top: ${it.top}, bottom: ${it.bottom}}")
            touchOffset.y.toInt() in it.top..it.bottom
        }?.let { task(it) }
    }

    override fun getKey(selectedItem: LazyListItemInfo) = selectedItem.key as? String

    override fun setInitial(selectedItem: LazyListItemInfo) {
        initialSelectedIndex = selectedItem.index
        initialSelectedTop = selectedItem.top
        initialSelectedBottom = selectedItem.bottom
    }

    override fun getSelectedItems(
        selectedItem: LazyListItemInfo,
        indexRange: (selectedItemIndex: Int) -> IntRange,
        passedItemKeysContainsOrNot: (String) -> Boolean
    ) = state.layoutInfo.visibleItemsInfo.filter {
        (it.key as? String)?.let { key ->
            it.index != initialSelectedIndex &&
                    it.index in indexRange(selectedItem.index) &&
                    passedItemKeysContainsOrNot(key)
        } ?: false
    }

    override fun getSelectedKeys(selectedItems: List<LazyListItemInfo>) =
        selectedItems.map { it.key as String }

    override fun setLast(selectedItem: LazyListItemInfo) {
        lastSelectedIndex = selectedItem.index
        lastSelectedTop = selectedItem.top
        lastSelectedBottom = selectedItem.bottom
    }

    override fun getDragDirection(touchOffset: Offset): DragDirection? =
        initialSelectedTop?.let { initialTop ->
            initialSelectedBottom?.let { initialBottom ->
                if (initialTop > touchOffset.y.toInt()) DragDirection.Up
                else if (initialBottom < touchOffset.y.toInt()) DragDirection.Down
                else DragDirection.None
            }
        }


    override fun getDragEndDirection(touchOffset: Offset): DragDirection {
        val dragEndDirection =
            lastSelectedTop?.let { lastTop ->
                lastSelectedBottom?.let { lastBottom ->
                    if (lastTop > touchOffset.y.toInt()) DragDirection.Up
                    else if (lastBottom < touchOffset.y.toInt()) DragDirection.Down
                    else DragDirection.None
                }
            }

        return if ((currentDragDirection == DragDirection.Down || currentDragDirection == DragDirection.None) && dragEndDirection == DragDirection.Up) DragDirection.Up
        else if ((currentDragDirection == DragDirection.Up || currentDragDirection == DragDirection.None) && dragEndDirection == DragDirection.Down) DragDirection.Down
        else DragDirection.None
    }

    override fun dragEnd() {
        initialSelectedTop = null
        initialSelectedBottom = null
        lastSelectedTop = null
        lastSelectedBottom = null
    }
}

val LazyListItemInfo.top: Int
    get() = offset + 20

val LazyListItemInfo.bottom: Int
    get() = top + size