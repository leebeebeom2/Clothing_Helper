package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput

enum class DragDirection {
    Up, Down, None
}

fun Modifier.dragSelect(
    dragSelector: BaseDragSelector<*>,
    onDragStart: (Boolean) -> Unit,
    onSelect: (key: String) -> Unit,
    onLongClick: (key: String) -> Unit
): Modifier {
    val realDragSelector = dragSelector as? ListDragSelector ?: dragSelector as GridDragSelector

    return this.then(Modifier.pointerInput(Unit) {
        detectDragGesturesAfterLongPress(onDragStart = { offset ->
            onDragStart(true)
            interceptOutOfBoundsChildEvents = true
            realDragSelector.dragSelectStart(offset, onLongClick)
        }, onDrag = { change, _ ->
            realDragSelector.onDrag(change.position, onSelect)
        }, onDragEnd = {
            dragEnd(onDragStart, realDragSelector)
        }, onDragCancel = {
            dragEnd(onDragStart, realDragSelector)
        })
    })
}

private fun PointerInputScope.dragEnd(
    onDragStart: (Boolean) -> Unit,
    realDragSelector: BaseDragSelector<out Any>
) {
    onDragStart(false)
    interceptOutOfBoundsChildEvents = false
    realDragSelector.onDragEnd()
}