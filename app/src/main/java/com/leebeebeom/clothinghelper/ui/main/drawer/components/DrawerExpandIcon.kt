package com.leebeebeom.clothinghelper.ui.main.drawer.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.components.DotProgressIndicator
import com.leebeebeom.clothinghelper.ui.main.composables.ExpandIcon

@Composable
fun DrawerExpandIcon(
    isLoading: () -> Boolean,
    isExpanded: () -> Boolean,
    toggleExpand: () -> Unit,
    show: () -> Boolean
) {
    if (isLoading())
        DotProgressIndicator(
            modifier = Modifier.padding(end = 4.dp),
            size = 4.dp,
            color = MaterialTheme.colors.surface.copy(ContentAlpha.disabled)
        )
    else if (show()) ExpandIcon(
        isExpanded = isExpanded, onClick = toggleExpand
    )
}