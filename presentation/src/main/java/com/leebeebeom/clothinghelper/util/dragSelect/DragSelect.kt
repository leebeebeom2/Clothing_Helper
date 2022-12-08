package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.dragSelect(
    dragSelector: BaseDragSelector<*>,
    onDragStart: (Boolean) -> Unit,
    onSelect: (keys: List<String>) -> Unit,
    onLongClick: (key: String) -> Unit
): Modifier {
    val castedDragSelector = dragSelector as? ListDragSelector ?: dragSelector as GridDragSelector

    return then(Modifier.pointerInput(Unit) {
        detectDragGesturesAfterLongPress(onDragStart = { offset ->
            onDragStart(true)
            interceptOutOfBoundsChildEvents = true
            castedDragSelector.dragSelectStart(offset, onLongClick)
        }, onDrag = { change, _ ->
            castedDragSelector.onDrag(change.position, onSelect)
        }, onDragEnd = {
            dragEnd(onDragStart, castedDragSelector)
        }, onDragCancel = {
            dragEnd(onDragStart, castedDragSelector)
        })
    })
}

private inline fun PointerInputScope.dragEnd(
    onDragStart: (Boolean) -> Unit,
    realDragSelector: BaseDragSelector<out LazyBaseItem>
) {
    onDragStart(false)
    interceptOutOfBoundsChildEvents = false
    realDragSelector.onDragEnd()
}