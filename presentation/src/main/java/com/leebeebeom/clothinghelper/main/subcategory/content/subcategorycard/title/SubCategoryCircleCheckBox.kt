package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.Anime
import com.leebeebeom.clothinghelper.main.base.composables.CircleCheckBox
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun SubCategoryCircleCheckBox(
    subCategory: () -> StableSubCategory,
    onClick: () -> Unit,
    selectedCategoryKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean
) {
    val isChecked by remember { derivedStateOf { selectedCategoryKeys().contains(subCategory().key) } }

    AnimatedVisibility(
        visible = isSelectMode(),
        enter = Anime.CircleCheckBox.expandIn,
        exit = Anime.CircleCheckBox.shrinkOut
    ) {
        CircleCheckBox(
            isChecked = { isChecked },
            modifier = Modifier.padding(start = 4.dp),
            onClick = onClick,
            size = 18.dp
        )
    }
}