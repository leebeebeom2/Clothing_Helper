package com.leebeebeom.clothinghelper.util

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.leebeebeom.clothinghelper.util.dragSelect.ListDragSelector

enum class DragDirection {
    Up, Down, None
}

fun Modifier.dragSelect(
    dragSelector: BaseDragSelector<*>,
    onDragStart: (Boolean) -> Unit,
    onSelect: (key: String) -> Unit,
    onLongClick: (key: String) -> Unit
): Modifier {
    val realDragSelector = dragSelector as ListDragSelector

    return this.then(Modifier.pointerInput(Unit) {
        detectDragGesturesAfterLongPress(onDragStart = { offset ->
            onDragStart(true)
            interceptOutOfBoundsChildEvents = true
            realDragSelector.dragSelectStart(offset, onLongClick)
        }, onDrag = { change, _ ->
            realDragSelector.onDrag(change.position, onSelect)
            realDragSelector.onDragEndMove(change.position, onSelect)
        }, onDragEnd = {
            onDragStart(false)
            interceptOutOfBoundsChildEvents = false
            realDragSelector.onDragEnd()
        }, onDragCancel = {
            onDragStart(false)
            interceptOutOfBoundsChildEvents = false
            realDragSelector.onDragEnd()
        })
    })
}

val LazyListItemInfo.top: Int
    get() = offset + 50

val LazyListItemInfo.bottom: Int
    get() = offset + size + 43