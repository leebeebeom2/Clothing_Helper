package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title.SubCategoryCardTitle
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubCategoryCard(
    subCategory: () -> StableSubCategory,
    onClick: () -> Unit,
    selectedCategoryKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean,
    folders: () -> ImmutableList<StableFolder>
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp), onClick = onClick) {
        Column {
            SubCategoryCardTitle(
                subCategory = subCategory,
                isExpanded = { isExpanded },
                onExpandIconClick = { isExpanded = !isExpanded },
                onCheckBoxClick = onClick,
                selectedCategoryKeys = selectedCategoryKeys,
                isSelectMode = isSelectMode,
                folders = folders
            )
            SubCategoryInfo { isExpanded }
        }
    }
}