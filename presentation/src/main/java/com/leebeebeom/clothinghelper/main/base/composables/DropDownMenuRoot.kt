package com.leebeebeom.clothinghelper.main.base.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun DropDownMenuRoot(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(20.dp))) {
        Surface {
            DropdownMenu(expanded = show(), onDismissRequest = onDismiss) {
                content()
            }
        }
    }
}