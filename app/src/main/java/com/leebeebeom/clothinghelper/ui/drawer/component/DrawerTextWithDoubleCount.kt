package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.SingleLineText

@Composable
fun RowScope.DrawerTextWithDoubleCount(
    // skippable
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
    // skippable
    modifier: Modifier = Modifier,
    text: () -> String,
    style: TextStyle,
    foldersSize: () -> Int,
    itemsSize: () -> Int,
) {
    val localFoldersSize by remember(foldersSize) { derivedStateOf(foldersSize) }
    val localItemsSize by remember(itemsSize) { derivedStateOf(itemsSize) }

    Column(
        modifier = modifier.weight(1f)
    ) {
        SingleLineText(text = text, style = style)
        SingleLineText(
            text = stringResource(
                id = R.string.folders_items, localFoldersSize, localItemsSize
            ),
            style = MaterialTheme.typography.caption.copy(
                LocalContentColor.current.copy(
                    ContentAlpha.disabled
                )
            )
        )
    }
}