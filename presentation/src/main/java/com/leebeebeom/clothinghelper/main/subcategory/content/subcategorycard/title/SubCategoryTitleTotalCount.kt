package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun SubCategoryTitleTotalCount() {
    SingleLineText( // TODO total count
        text = "(10)", style = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    )
}