package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.leebeebeom.clothinghelper.ui.main.component.ExpandIcon

const val DrawerExpandIconTag = "drawer expand icon"

@Composable // skippable
fun DrawerExpandIcon(
    expanded: () -> Boolean,
    toggleExpand: () -> Unit,
    dataSize: () -> Int
) {
    val show by remember { derivedStateOf { dataSize() > 0 } }

    if (show) ExpandIcon(
        modifier = Modifier.testTag(DrawerExpandIconTag),
        isExpanded = expanded, onClick = toggleExpand
    )
}