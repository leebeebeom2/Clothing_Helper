package com.leebeebeom.clothinghelper.main.root.components

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.map.StableSizeChart
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun DrawerCount(
    folders: () -> ImmutableList<*>,
    charts: () -> ImmutableList<*> = { emptyList<StableSizeChart>().toImmutableList() } // TODO
) {
    val folderSize by remember { derivedStateOf { folders().size } }
    val chartSize by remember { derivedStateOf { charts().size } }

    SingleLineText(
        text = stringResource(
            id = R.string.folders_charts,
            formatArgs = arrayOf(folderSize, chartSize)
        ),
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(ContentAlpha.disabled)
        )
    )
}