package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.main.base.composables.ExpandIcon
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun SubCategoryCardTitle(
    subCategory: () -> StableSubCategory,
    isExpanded: () -> Boolean,
    onExpandIconClick: () -> Unit,
    onCheckBoxClick: () -> Unit,
    selectedCategoryKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean,
    folders: () -> ImmutableList<StableFolder>
) {
    Surface(elevation = 4.dp) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, end = 0.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubCategoryCircleCheckBox(
                isSelectMode = isSelectMode,
                subCategory = subCategory,
                onClick = onCheckBoxClick,
                selectedCategoryKeys = selectedCategoryKeys
            )
            SubCategoryTitleAnimateSpacer(isSelectMode = isSelectMode)
            Column(modifier = Modifier.weight(1f)) {
                SubCategoryName { subCategory().name }
                SimpleHeightSpacer(dp = 4)
                SubCategoryCardCount(folders)
            }
            ExpandIcon(
                modifier = Modifier.size(30.dp),
                isExpanded = isExpanded,
                onClick = onExpandIconClick
            )
        }
    }
}