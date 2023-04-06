package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.SingleLineText

@Composable // skippable
fun DrawerCount(
    folderSize: () -> Int,
    itemSize: () -> Int
) {
    SingleLineText(
        text = stringResource(
            id = R.string.folders_items,
            folderSize(), itemSize()
        ),
        style = drawerCountStyle()
    )
}

@Composable // skippable
fun DrawerCount(dataSize: () -> Int) {
    SingleLineText(
        modifier = Modifier.padding(start = 4.dp),
        text = "(${dataSize()})",
        style = drawerCountStyle()
    )
}

@Composable
private fun drawerCountStyle() =
    MaterialTheme.typography.caption.copy(LocalContentColor.current.copy(ContentAlpha.disabled))