package com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.theme.BlackA3

@Composable
fun DrawerDropdownMenu( // skippable
    state: DrawerDropdownMenuState,
    onDismiss: () -> Unit,
    dropdownMenuItems: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val offset by remember {
        derivedStateOf {
            with(density) {
                DpOffset(
                    x = state.longClickOffsetX.toDp(),
                    y = state.longClickOffsetY.toDp() - state.itemHeight.toDp()
                )
            }
        }
    }

    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            surface = BlackA3,
            onSurface = Color.White
        )
    ) {
        DropdownMenu(
            modifier = Modifier.widthIn(120.dp),
            expanded = state.showDropdownMenu,
            onDismissRequest = onDismiss,
            offset = offset
        ) {
            dropdownMenuItems()
        }
    }
}

@Stable
interface DrawerDropdownMenuState {
    val showDropdownMenu: Boolean
    val longClickOffsetX: Float
    val longClickOffsetY: Float
    val itemHeight: Int
}