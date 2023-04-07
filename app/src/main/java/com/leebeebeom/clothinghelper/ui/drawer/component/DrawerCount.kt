package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.SingleLineText

@Composable // skippable
fun DrawerCount(
    foldersSize: () -> Int, itemsSize: () -> Int
) {
    val localFoldersSize by remember(foldersSize) { derivedStateOf(foldersSize) }
    val localItemsSize by remember(itemsSize) { derivedStateOf(foldersSize) }

    SingleLineText(
        text = stringResource(
            id = R.string.folders_items, localFoldersSize, localItemsSize
        ), style = drawerCountStyle()
    )
}

@Composable // skippable
fun DrawerCount(dataSize: () -> Int) {
    val localDataSize by remember(dataSize) { derivedStateOf(dataSize) }

    SingleLineText(
        modifier = Modifier.padding(start = 4.dp),
        text = "($localDataSize)",
        style = drawerCountStyle()
    )
}

@Composable
private fun drawerCountStyle() =
    MaterialTheme.typography.caption.copy(LocalContentColor.current.copy(ContentAlpha.disabled))