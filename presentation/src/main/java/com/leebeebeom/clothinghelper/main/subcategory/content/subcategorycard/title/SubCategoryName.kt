package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun SubCategoryName(name: () -> String) {
    SingleLineText(
        modifier = Modifier.widthIn(max = 300.dp),
        text = name(),
        style = MaterialTheme.typography.subtitle1
    )
}