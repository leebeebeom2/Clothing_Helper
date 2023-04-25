package com.leebeebeom.clothinghelper.ui.drawer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuState

@Stable
interface DrawerItemState {
    val expanded: Boolean
}

open class MutableDrawerItemState(initialExpand: Boolean = false) : DrawerItemState { // stable
    final override var expanded by mutableStateOf(initialExpand)
        private set

    fun toggleExpand() {
        expanded = !expanded
    }

    fun expand() {
        expanded = true
    }

    companion object {
        val Saver = listSaver(
            save = { listOf(it.expanded) },
            restore = { MutableDrawerItemState(it[0]) }
        )
    }
}

@Composable
fun rememberDrawerItemState() =
    rememberSaveable(saver = MutableDrawerItemState.Saver) { MutableDrawerItemState() }

class MutableDrawerDropdownMenuState(
    // stable
    initialExpand: Boolean = false,
    initialShowDropdownMenu: Boolean = false,
    initialLongClickOffsetX: Float = 0f,
    initialLongClickOffsetY: Float = 0f,
    initialItemHeight: Int = 0,
) : MutableDrawerItemState(initialExpand), DrawerDropdownMenuState {

    override var showDropdownMenu by mutableStateOf(initialShowDropdownMenu)
        private set
    override var longClickOffsetX by mutableStateOf(initialLongClickOffsetX)
        private set
    override var longClickOffsetY by mutableStateOf(initialLongClickOffsetY)
        private set
    override var itemHeight by mutableStateOf(initialItemHeight)
        private set

    fun onLongClick(offset: Offset) {
        longClickOffsetX = offset.x
        longClickOffsetY = offset.y
        showDropdownMenu = true
    }

    fun onSizeChanged(size: IntSize) {
        itemHeight = size.height
    }

    fun onDismissDropdownMenu() {
        showDropdownMenu = false
    }

    companion object {
        val Saver = listSaver<MutableDrawerDropdownMenuState, Any>(
            save = {
                listOf(
                    it.expanded,
                    it.showDropdownMenu,
                    it.longClickOffsetX,
                    it.longClickOffsetY,
                    it.itemHeight
                )
            },
            restore = {
                MutableDrawerDropdownMenuState(
                    initialExpand = it[0] as Boolean,
                    initialShowDropdownMenu = it[1] as Boolean,
                    initialLongClickOffsetX = it[2] as Float,
                    initialLongClickOffsetY = it[3] as Float,
                    initialItemHeight = it[4] as Int
                )
            }
        )
    }
}

@Composable
fun rememberDrawerItemDropdownMenuState() =
    rememberSaveable(saver = MutableDrawerDropdownMenuState.Saver) { MutableDrawerDropdownMenuState() }