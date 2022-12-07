package com.leebeebeom.clothinghelper.main.root.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainTotalCount(
    items: () -> ImmutableList<*>,
    isLoading: () -> Boolean,
) {
    SingleLineText(
        modifier = Modifier.padding(start = 4.dp),
        text = if (isLoading()) "" else "(${items().size})",
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(ContentAlpha.disabled)
        )
    )
}