package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SubCategoryCardCount(folders: () -> ImmutableList<StableFolder>) {
    SingleLineText(
        text = stringResource(
            id = R.string.folders_charts,
            formatArgs = arrayOf(folders().size, 0)
        ),
        style = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    )
}