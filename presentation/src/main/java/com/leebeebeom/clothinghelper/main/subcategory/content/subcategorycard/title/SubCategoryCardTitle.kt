package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.main.base.components.ExpandIcon
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun SubCategoryCardTitle(
    subCategory: () -> StableSubCategory,
    isExpanded: () -> Boolean,
    onExpandIconClick: () -> Unit,
    onCheckBoxClick: () -> Unit,
    selectedCategoryKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean
) {
    Surface(elevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp)
                .padding(end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                SubCategoryCircleCheckBox(
                    isSelectMode = isSelectMode,
                    subCategory = subCategory,
                    onClick = onCheckBoxClick,
                    selectedCategoryKeys = selectedCategoryKeys
                )
                SubCategoryTitleAnimateSpacer(isSelectMode = isSelectMode)
                SubCategoryName(name = { subCategory().name })
                SimpleWidthSpacer(dp = 4)
                SubCategoryTitleTotalCount()
            }

            ExpandIcon(
                modifier = Modifier.size(24.dp),
                isExpanded = isExpanded,
                onClick = onExpandIconClick
            )
        }
    }
}