package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.SingleLineText

@Composable // skippable
fun DrawerCount(
    folderSize: () -> Int,
    chartSize: () -> Int
) {
    SingleLineText(
        text = stringResource(
            id = R.string.folders_charts,
            arrayOf(folderSize(), chartSize())
        ),
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(ContentAlpha.disabled)
        )
    )
}