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
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    style: TextStyle,
    foldersSize: () -> Int,
    itemsSize: () -> Int,
) {
    val localText = stringResource(id = text)

    DrawerTextWithDoubleCount(
        modifier = modifier,
        text = { localText },
        style = style,
        foldersSize = foldersSize,
        itemsSize = itemsSize
    )
}


@Composable
fun RowScope.DrawerTextWithDoubleCount(
    modifier: Modifier = Modifier,
    text: () -> String,
    style: TextStyle,
    foldersSize: () -> Int,
    itemsSize: () -> Int,
) {
    Column(
        modifier = modifier.weight(1f)
    ) {
        SingleLineText(text = text, style = style)
        DrawerCount(foldersSize = foldersSize, itemsSize = itemsSize)
    }
}
