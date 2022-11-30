package com.leebeebeom.clothinghelper.main.base.composables

import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable

@Composable
fun DropDownMenuRoot(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    DropdownMenu(expanded = show(), onDismissRequest = onDismiss) {
        content()
    }
}