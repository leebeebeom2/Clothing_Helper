package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

abstract class BaseDragSelector<T : BaseSelectedItem>(
    private val haptic: HapticFeedback
) {
    private val passedItems: LinkedHashSet<T> = linkedSetOf()

    fun dragSelectStart(touchOffset: Offset, onLongClick: (key: String) -> Unit) =
        getSelectedItem(touchOffset) { selectedItem ->
            (selectedItem.key as? String)?.let { key ->
                passedItems.add(selectedItem)
                onLongClick(key)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }

    fun onDrag(touchOffset: Offset, onSelect: (String) -> Unit) {
        passedItems.firstOrNull()?.let { initialItem ->
            getSelectedItem(touchOffset) { selectedItem ->
                if (isDragDown(initialItem = initialItem, selectedItem = selectedItem))
                    dragDown(
                        selectedItem = selectedItem,
                        onSelect = onSelect,
                        initialItem = initialItem
                    )
                else if (isDragUp(initialItem = initialItem, selectedItem = selectedItem))
                    dragUp(
                        selectedItem = selectedItem,
                        onSelect = onSelect,
                        initialItem = initialItem
                    )
                else if (passedItems.first().index == selectedItem.index) {
                    passedItems.drop(1).forEach { onSelect(it.key) }
                    val initial = passedItems.first()
                    passedItems.clear()
                    passedItems.add(initial)
                }

            }
        }
    }

    private fun dragUp(selectedItem: T, onSelect: (String) -> Unit, initialItem: T) {
        if (isDragUpForward(selectedItem)) { // 드래그 업 정방향
            initPassedItems(onSelect) { it.index > initialItem.index } // 패스드 아이템 초기화
            forward(
                selectedItem = selectedItem,
                onSelect = onSelect,
                range = selectedItem.index until initialItem.index
            )
        } else if (isDragUpBackward(selectedItem)) { // 드래그 업 역방향
            backward(
                selectedItem = selectedItem,
                onSelect = onSelect,
                range = passedItems.last().index until selectedItem.index
            )
        }
    }

    private fun dragDown(
        selectedItem: T,
        onSelect: (String) -> Unit,
        initialItem: T
    ) {
        if (dragDownForward(selectedItem)) { // 드래그 다운 정방향
            initPassedItems(onSelect) { it.index < initialItem.index } // 패스드 아이템 초기화
            forward(
                selectedItem = selectedItem,
                onSelect = onSelect,
                range = initialItem.index + 1..selectedItem.index
            )
        } else if (dragDownBackward(selectedItem)) { // 드래그 다운 역방향
            backward(
                selectedItem = selectedItem,
                onSelect = onSelect,
                range = selectedItem.index + 1..passedItems.last().index
            )
        }
    }

    /**
     * @param range selectedItem.index + 1..passedItems.last().index <- drag down
     *              passedItems.last().index until selectedItem.index <- drag up
     */
    private fun backward(selectedItem: T, onSelect: (String) -> Unit, range: IntRange) {
        val selectedItems = getSelectedItems(
            selectedItem = selectedItem,
            indexRange = range,
            passedItemsContainsOrNot = { passedItems.contains(it) }
        )

        passedItems.removeAll(selectedItems)
        selectedItems.forEach { onSelect(it.key) }
    }

    /**
     * @param range initialItem.Index + 1..selectedItem.index <- drag down
     *              selectedItem.index until initialItem.Index <- drag up
     */
    private fun forward(selectedItem: T, onSelect: (String) -> Unit, range: IntRange) {
        val selectedItems = getSelectedItems(
            selectedItem = selectedItem,
            indexRange = range,
            passedItemsContainsOrNot = { !passedItems.contains(it) }
        )

        passedItems.addAll(selectedItems)
        selectedItems.forEach { onSelect(it.key) }
    }

    /**
     * @param predicate it.index > initialItem.index or it.index < initialItem.index
     */
    private fun initPassedItems(onSelect: (String) -> Unit, predicate: (passedItem: T) -> Boolean) {
        if (passedItems.size > 1) {
            val previousPassedItems = passedItems.filter(predicate)
            passedItems.removeAll(previousPassedItems.toSet())
            previousPassedItems.forEach { onSelect(it.key) }
        }
    }

    private fun isDragDown(initialItem: T, selectedItem: T) =
        initialItem.index < selectedItem.index

    private fun dragDownForward(selectedItem: T) =
        passedItems.last().index < selectedItem.index

    private fun dragDownBackward(selectedItem: T) =
        passedItems.last().index > selectedItem.index

    private fun isDragUp(initialItem: T, selectedItem: T) =
        initialItem.index > selectedItem.index

    private fun isDragUpForward(selectedItem: T) =
        passedItems.last().index > selectedItem.index

    private fun isDragUpBackward(selectedItem: T) =
        passedItems.last().index < selectedItem.index

    fun onDragEnd() = passedItems.clear()

    protected abstract fun getSelectedItem(touchOffset: Offset, task: (T) -> Unit)
    protected abstract fun getSelectedItems(
        selectedItem: T,
        indexRange: IntRange,
        passedItemsContainsOrNot: (T) -> Boolean
    ): Set<T>
}