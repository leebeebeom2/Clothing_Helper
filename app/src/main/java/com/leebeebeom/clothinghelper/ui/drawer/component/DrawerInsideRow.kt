package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RowScope.DrawerInsideRow(
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}