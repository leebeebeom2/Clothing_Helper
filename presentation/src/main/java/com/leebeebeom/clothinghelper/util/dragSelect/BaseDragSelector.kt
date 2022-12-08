package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

abstract class BaseDragSelector<T : LazyBaseItem>(
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

    fun onDrag(touchOffset: Offset, onSelect: (List<String>) -> Unit) {
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
                    onSelect(passedItems.drop(1).map { it.key })
                    val initial = passedItems.first()
                    passedItems.clear()
                    passedItems.add(initial)
                }

            }
        }
    }

    private fun dragUp(selectedItem: T, onSelect: (List<String>) -> Unit, initialItem: T) {
        if (isDragUpForward(selectedItem)) { // 드래그 업 정방향
            initPassedItems(onSelect) { it.index > initialItem.index } // 패스드 아이템 초기화
            forward(
                onSelect = onSelect,
                range = selectedItem.index until initialItem.index // 선택된 아이템부터 최초 아이템 전까지
            )
        } else if (isDragUpBackward(selectedItem)) { // 드래그 업 역방향
            backward(
                onSelect = onSelect,
                range = passedItems.last().index until selectedItem.index // 마지막 아이템부터 선택된 아이템 전까지
            )
        }
    }

    private fun dragDown(
        selectedItem: T, onSelect: (List<String>) -> Unit, initialItem: T
    ) {
        if (isDragDownForward(selectedItem)) { // 드래그 다운 정방향
            initPassedItems(onSelect) { it.index < initialItem.index } // 패스드 아이템 초기화
            forward(
                onSelect = onSelect,
                range = initialItem.index + 1..selectedItem.index // 최초 아이템 부터 마지막 아이템 까지
            )
        } else if (isDragDownBackward(selectedItem)) { // 드래그 다운 역방향
            backward(
                onSelect = onSelect,
                range = selectedItem.index + 1..passedItems.last().index // 선택된 아이템 다음 부터 마지막 아이템 까지
            )
        }
    }

    /**
     * @param range selectedItem.index + 1..passedItems.last().index <- drag down
     *              passedItems.last().index until selectedItem.index <- drag up
     */
    private fun backward(onSelect: (List<String>) -> Unit, range: IntRange) {
        val selectedItems = getSelectedItems(
            indexRange = range,
            passedItemsContainsOrNot = { passedItems.contains(it) } // 역방향이기 때문에 passedItems에 있는 아이템만
        )

        passedItems.removeAll(selectedItems.toSet())
        onSelect(selectedItems.map { it.key })
    }

    /**
     * @param range initialItem.Index + 1..selectedItem.index <- drag down
     *              selectedItem.index until initialItem.Index <- drag up
     */
    private fun forward(onSelect: (List<String>) -> Unit, range: IntRange) {
        val selectedItems = getSelectedItems(
            indexRange = range,
            passedItemsContainsOrNot = { !passedItems.contains(it) } // 최초 선택이기 때문에 passedItems에 없는 아이템만
        )

        passedItems.addAll(selectedItems)
        onSelect(selectedItems.map { it.key })
    }

    /**
     * 최초 아이템이 중간일 경우 아래로 드래그 후 위로 드래그 시 최초 아이템의 밑 아이템 삭제(반대도 마찬가지)
     *
     * @param predicate it.index > initialItem.index or it.index < initialItem.index
     */
    private fun initPassedItems(
        onSelect: (List<String>) -> Unit,
        predicate: (passedItem: T) -> Boolean
    ) {
        if (passedItems.any(predicate)) {
            val oppositePassedItems = passedItems.filter(predicate)
            passedItems.removeAll(oppositePassedItems.toSet())
            onSelect(oppositePassedItems.map { it.key })
        }
    }


    private fun isDragDown(initialItem: T, selectedItem: T) = initialItem.index < selectedItem.index

    private fun isDragUp(initialItem: T, selectedItem: T) = initialItem.index > selectedItem.index

    private fun isDragUpForward(selectedItem: T) = passedItems.last().index > selectedItem.index

    private fun isDragUpBackward(selectedItem: T) = passedItems.last().index < selectedItem.index

    private fun isDragDownForward(selectedItem: T) = passedItems.last().index < selectedItem.index

    private fun isDragDownBackward(selectedItem: T) = passedItems.last().index > selectedItem.index

    fun onDragEnd() = passedItems.clear()

    private fun getSelectedItem(touchOffset: Offset, task: (T) -> Unit) {
        visibleItemsInfo.firstOrNull { visibleItem ->
            (visibleItem.key as? String)?.let {
                selectedItemPredicate(touchOffset = touchOffset, visibleItem = visibleItem)
            } ?: false
        }?.let { selectedItem ->
            task(selectedItem)
        }
    }

    private fun getSelectedItems(
        indexRange: IntRange,
        passedItemsContainsOrNot: (T) -> Boolean
    ) = visibleItemsInfo.filter { visibleItem ->
        (visibleItem.key as? String)?.let { _ ->
            visibleItem.index in indexRange && passedItemsContainsOrNot(visibleItem)
        } ?: false
    }

    abstract val visibleItemsInfo: List<T>
    abstract fun selectedItemPredicate(touchOffset: Offset, visibleItem: T): Boolean
}