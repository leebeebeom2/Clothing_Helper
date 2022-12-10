package com.leebeebeom.clothinghelper.ui.main.root.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.composable.DotProgressIndicator
import com.leebeebeom.clothinghelper.ui.main.composables.ExpandIcon
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerExpandIcon(
    isLoading: () -> Boolean,
    isExpanded: () -> Boolean,
    onClick: () -> Unit,
    items: () -> ImmutableList<*>
) {
    val show by remember { derivedStateOf { items().size > 0 } }

    if (isLoading()) DotProgressIndicator(
        modifier = Modifier.padding(end = 4.dp),
        size = 4.dp,
        color = MaterialTheme.colors.surface.copy(ContentAlpha.disabled)
    )
    else if (show) ExpandIcon(
        isExpanded = isExpanded, onClick = onClick
    )
}