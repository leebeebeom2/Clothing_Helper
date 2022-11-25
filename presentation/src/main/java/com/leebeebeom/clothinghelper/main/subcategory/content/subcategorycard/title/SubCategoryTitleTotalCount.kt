package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SubCategoryTitleTotalCount(folders: () -> ImmutableList<StableFolder>) {
    SingleLineText(
        text = "(${folders().size})", style = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    )
}