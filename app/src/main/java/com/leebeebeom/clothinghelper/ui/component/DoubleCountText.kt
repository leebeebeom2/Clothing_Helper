package com.leebeebeom.clothinghelper.ui.component

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.leebeebeom.clothinghelper.R

@Composable
fun DoubleCountText(
    foldersSize: () -> Int,
    itemsSize: () -> Int
) {
    val localFoldersSize by remember(foldersSize) { derivedStateOf(foldersSize) }
    val localItemsSize by remember(itemsSize) { derivedStateOf(itemsSize) }

    SingleLineText(
        text = stringResource(
            id = R.string.folders_items, localFoldersSize, localItemsSize
        ),
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(ContentAlpha.disabled)
        )
    )
}