package com.leebeebeom.clothinghelper.util

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput

enum class DragDirection {
    Up, Down, None
}

fun Modifier.dragSelect(
    state: LazyListState,
    onDragStart: (Boolean) -> Unit,
    onSelect: (key: String) -> Unit,
    onLongClick: (key: String) -> Unit,
    haptic: HapticFeedback
): Modifier {
    var initialSelectedIndex: Int? = null
    var initialSelectedTop: Int? = null
    var initialSelectedBottom: Int? = null
    var lastSelectedIndex: Int? = null
    var lastSelectedTop: Int? = null
    var lastSelectedBottom: Int? = null
    val passedItemKeys: LinkedHashSet<String> = linkedSetOf()
    var dragDirection: DragDirection = DragDirection.None

    fun getDragDirection(
        top: Int,
        bottom: Int,
        touchOffset: Offset
    ): DragDirection {
        return if (top > touchOffset.y.toInt()) DragDirection.Up
        else if (bottom < touchOffset.y.toInt()) DragDirection.Down
        else DragDirection.None
    }

    fun getDragEndDirection(
        lastTop: Int,
        lastBottom: Int,
        touchOffset: Offset
    ): DragDirection {
        val dragEndDirection = getDragDirection(lastTop, lastBottom, touchOffset)
        return if ((dragDirection == DragDirection.Down || dragDirection == DragDirection.None) && dragEndDirection == DragDirection.Up
        ) DragDirection.Up
        else if ((dragDirection == DragDirection.Up || dragDirection == DragDirection.None) && dragEndDirection == DragDirection.Down
        ) DragDirection.Down
        else DragDirection.None
    }

    fun LazyListState.getSelectedItem(
        touchOffset: Offset,
        task: (LazyListItemInfo) -> Unit
    ) {
        layoutInfo.visibleItemsInfo.firstOrNull {
            touchOffset.y.toInt() in it.realOffset..it.offsetEnd
        }?.let { task(it) }
    }

    fun dragSelectStart(
        touchOffset: Offset, onLongClick: (key: String) -> Unit
    ) {
        state.getSelectedItem(touchOffset) { selectedItem ->
            (selectedItem.key as? String)?.let {
                initialSelectedIndex = selectedItem.index
                initialSelectedTop = selectedItem.realOffset
                initialSelectedBottom = selectedItem.offsetEnd
                passedItemKeys.add(it)
                onLongClick(it)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }

    fun onDragStart(
        touchOffset: Offset,
        range: (selectedItemIndex: Int) -> IntRange,
        passedItemKeysContainsOrNot: (String) -> Boolean,
        onSelect: (String) -> Unit,
        passedItemKeysAddOrRemove: (Set<String>) -> Unit,
        selectedItemFirstOrLast: (List<LazyListItemInfo>) -> LazyListItemInfo?
    ) {
        state.getSelectedItem(touchOffset) { selectedItem ->
            val selectedItems = state.layoutInfo.visibleItemsInfo.filter {
                (it.key as? String)?.let { key ->
                    it.index != initialSelectedIndex &&
                            it.index in range(selectedItem.index) &&
                            passedItemKeysContainsOrNot(key)
                } ?: false
            }
            val selectedKeys = selectedItems.map { it.key as String }
            passedItemKeysAddOrRemove(selectedKeys.toSet())
            selectedKeys.forEach(onSelect)
            selectedItemFirstOrLast(selectedItems)?.let {
                lastSelectedIndex = it.index
                lastSelectedTop = it.realOffset
                lastSelectedBottom = it.offsetEnd
            }
        }
    }

    fun onDrag(touchOffset: Offset, onSelect: (String) -> Unit) {
        initialSelectedIndex?.let { index ->
            initialSelectedTop?.let { top ->
                initialSelectedBottom?.let { bottom ->
                    dragDirection = getDragDirection(top, bottom, touchOffset)

                    if (dragDirection == DragDirection.Down)
                        onDragStart(
                            touchOffset = touchOffset,
                            range = { index..it },
                            passedItemKeysContainsOrNot = { !passedItemKeys.contains(it) },
                            onSelect = onSelect,
                            passedItemKeysAddOrRemove = passedItemKeys::addAll,
                            selectedItemFirstOrLast = { it.lastOrNull() }
                        )
                    else if (dragDirection == DragDirection.Up)
                        onDragStart(
                            touchOffset = touchOffset,
                            range = { it..index },
                            passedItemKeysContainsOrNot = { !passedItemKeys.contains(it) },
                            onSelect = onSelect,
                            passedItemKeysAddOrRemove = passedItemKeys::addAll,
                            selectedItemFirstOrLast = { it.firstOrNull() }
                        )
                }
            }
        }

    }

    fun onDragEndMove(touchOffset: Offset, onSelect: (key: String) -> Unit) {
        lastSelectedIndex?.let { index ->
            lastSelectedTop?.let { top ->
                lastSelectedBottom?.let { bottom ->
                    val endDragDirection = getDragEndDirection(top, bottom, touchOffset)
                    println(endDragDirection)
                    if (endDragDirection == DragDirection.Up)
                        onDragStart(
                            touchOffset = touchOffset,
                            range = { it..index },
                            passedItemKeysContainsOrNot = passedItemKeys::contains,
                            onSelect = onSelect,
                            passedItemKeysAddOrRemove = passedItemKeys::removeAll,
                            selectedItemFirstOrLast = { it.lastOrNull() }
                        )
                    else if (endDragDirection == DragDirection.Down)
                        onDragStart(
                            touchOffset = touchOffset,
                            range = { index..it },
                            passedItemKeysContainsOrNot = passedItemKeys::contains,
                            onSelect = onSelect,
                            passedItemKeysAddOrRemove = passedItemKeys::removeAll,
                            selectedItemFirstOrLast = { it.firstOrNull() }
                        )
                }
            }
        }
    }

    fun onDragEnd() {
        initialSelectedIndex = null
        initialSelectedTop = null
        initialSelectedBottom = null
        lastSelectedIndex = null
        lastSelectedTop = null
        lastSelectedBottom = null
        dragDirection = DragDirection.None
        passedItemKeys.clear()
    }


    return this.then(Modifier.pointerInput(Unit) {
        detectDragGesturesAfterLongPress(onDragStart = { offset ->
            onDragStart(true)
            interceptOutOfBoundsChildEvents = true
            dragSelectStart(offset, onLongClick)
        }, onDrag = { change, _ ->
            onDrag(change.position, onSelect)
            onDragEndMove(change.position, onSelect)
        }, onDragEnd = {
            onDragStart(false)
            interceptOutOfBoundsChildEvents = false
            onDragEnd()
        }, onDragCancel = {
            onDragStart(false)
            interceptOutOfBoundsChildEvents = false
            onDragEnd()
        })
    })
}

val LazyListItemInfo.realOffset: Int
    get() = offset + 50

val LazyListItemInfo.offsetEnd: Int
    get() = offset + size + 43