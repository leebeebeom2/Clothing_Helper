package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.leebeebeom.clothinghelper.ui.component.SingleLineText

@Composable
fun RowScope.DrawerTextWithDoubleCount(
    modifier: Modifier,
    @StringRes text: Int,
    style: TextStyle,
    folderSize: () -> Int,
    itemSize: () -> Int,
) {
    val localText = stringResource(id = text)

    DrawerTextWithDoubleCount(
        modifier = modifier,
        text = { localText },
        style = style,
        folderSize = folderSize,
        itemSize = itemSize
    )
}


@Composable
fun RowScope.DrawerTextWithDoubleCount(
    modifier: Modifier,
    text: () -> String,
    style: TextStyle,
    folderSize: () -> Int,
    itemSize: () -> Int,
) {
    Column(
        modifier = modifier.weight(1f)
    ) {
        SingleLineText(text = text, style = style)
        DrawerCount(folderSize = folderSize, itemSize = itemSize)
    }
}
