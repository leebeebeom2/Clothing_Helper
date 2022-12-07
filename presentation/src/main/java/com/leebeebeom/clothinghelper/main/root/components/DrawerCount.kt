package com.leebeebeom.clothinghelper.main.root.components

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerCount(
    items: () -> ImmutableList<*>
) {
    SingleLineText(
        text = stringResource(
            id = R.string.folders_charts,
            formatArgs = arrayOf(items().size, 0)
        ),
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(ContentAlpha.disabled)
        )
    )
}