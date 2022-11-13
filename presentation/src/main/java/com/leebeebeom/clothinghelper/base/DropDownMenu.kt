package com.leebeebeom.clothinghelper.base

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun DropDownMenuRoot(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    background: Color = MaterialTheme.colors.surface,
    offsetX: () -> Float = { 0f },
    offsetY: () -> Float = { 0f },
    content: @Composable () -> Unit,
) {
    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(20.dp))) {
        Surface(color = background) {
            DropdownMenu(
                expanded = show(),
                onDismissRequest = onDismiss,
                offset = DpOffset(offsetX().dp, offsetY().dp)
            ) {
                content()
            }
        }
    }
}