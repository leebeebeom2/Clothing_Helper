package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubCategoryTitleAnimateSpacer(isSelectMode: () -> Boolean) {
    val width by animateDpAsState(targetValue = if (isSelectMode()) 2.dp else 12.dp)

    Spacer(modifier = Modifier.width(width))
}