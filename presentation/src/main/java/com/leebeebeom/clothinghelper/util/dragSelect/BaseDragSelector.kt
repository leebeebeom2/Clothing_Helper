package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

abstract class BaseDragSelector<T>(
    private val haptic: HapticFeedback
) {
    protected var dragDirection: DragDirection? = DragDirection.None
    private val passedItemKeys: LinkedHashSet<String> = linkedSetOf()
    protected var lastSelectedIndex: Int? = null
    protected var initialSelectedIndex: Int? = null

    fun dragSelectStart(touchOffset: Offset, onLongClick: (key: String) -> Unit) {
        getSelectedItem(touchOffset) { selectedItem ->
            getKey(selectedItem)?.let {
                setInitial(selectedItem)
                passedItemKeys.add(it)
                onLongClick(it)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }

    fun onDrag(touchOffset: Offset, onSelect: (String) -> Unit) {
        initialSelectedIndex?.let { index ->
            dragDirection = getDragDirection(touchOffset)

            if (dragDirection == DragDirection.Down)
                onDragStart(
                    touchOffset = touchOffset,
                    indexRange = { index..it },
                    passedItemKeysContainsOrNot = { !passedItemKeys.contains(it) },
                    onSelect = onSelect,
                    passedItemKeysAddOrRemove = passedItemKeys::addAll,
                    selectedItemFirstOrLast = { it.lastOrNull() }
                )
            else if (dragDirection == DragDirection.Up)
                onDragStart(
                    touchOffset = touchOffset,
                    indexRange = { it..index },
                    passedItemKeysContainsOrNot = { !passedItemKeys.contains(it) },
                    onSelect = onSelect,
                    passedItemKeysAddOrRemove = passedItemKeys::addAll,
                    selectedItemFirstOrLast = { it.firstOrNull() }
                )
        }
    }

    fun onDragEndMove(touchOffset: Offset, onSelect: (key: String) -> Unit) {
        lastSelectedIndex?.let { index ->
            val dragEndDirection = getDragEndDirection(touchOffset)

            if (dragEndDirection == DragDirection.Up)
                onDragStart(
                    touchOffset = touchOffset,
                    indexRange = { it..index },
                    passedItemKeysContainsOrNot = passedItemKeys::contains,
                    onSelect = onSelect,
                    passedItemKeysAddOrRemove = passedItemKeys::removeAll,
                    selectedItemFirstOrLast = { it.lastOrNull() }
                )
            else if (dragEndDirection == DragDirection.Down)
                onDragStart(
                    touchOffset = touchOffset,
                    indexRange = { index..it },
                    passedItemKeysContainsOrNot = passedItemKeys::contains,
                    onSelect = onSelect,
                    passedItemKeysAddOrRemove = passedItemKeys::removeAll,
                    selectedItemFirstOrLast = { it.firstOrNull() }
                )
        }
    }

    private fun onDragStart(
        touchOffset: Offset,
        indexRange: (selectedItemIndex: Int) -> IntRange,
        passedItemKeysContainsOrNot: (String) -> Boolean,
        onSelect: (String) -> Unit,
        passedItemKeysAddOrRemove: (Set<String>) -> Unit,
        selectedItemFirstOrLast: (List<T>) -> T?
    ) {
        getSelectedItem(touchOffset) { selectedItem ->
            val selectedItems =
                getSelectedItems(selectedItem, indexRange, passedItemKeysContainsOrNot)
            val selectedKeys = getSelectedKeys(selectedItems)
            passedItemKeysAddOrRemove(selectedKeys.toSet())
            selectedKeys.forEach(onSelect)
            selectedItemFirstOrLast(selectedItems)?.let { setLast(it) }
        }
    }

    fun onDragEnd() {
        dragEnd()
        initialSelectedIndex = null
        lastSelectedIndex = null
        dragDirection = DragDirection.None
        passedItemKeys.clear()
    }

    protected abstract fun getSelectedItem(touchOffset: Offset, task: (T) -> Unit)
    protected abstract fun getKey(selectedItem: T): String?
    protected abstract fun setInitial(selectedItem: T)
    protected abstract fun setLast(selectedItem: T)
    protected abstract fun getSelectedItems(
        selectedItem: T,
        indexRange: (selectedItemIndex: Int) -> IntRange,
        passedItemKeysContainsOrNot: (String) -> Boolean
    ): List<T>

    protected abstract fun getSelectedKeys(selectedItems: List<T>): List<String>
    protected abstract fun getDragDirection(touchOffset: Offset): DragDirection?
    protected abstract fun getDragEndDirection(touchOffset: Offset): DragDirection
    protected abstract fun dragEnd()
}