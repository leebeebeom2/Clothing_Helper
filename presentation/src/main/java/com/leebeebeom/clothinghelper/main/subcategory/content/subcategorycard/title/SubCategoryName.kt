package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun SubCategoryName(name: () -> String) {
    SingleLineText(
        text = name(),
        style = MaterialTheme.typography.subtitle1
    )
}