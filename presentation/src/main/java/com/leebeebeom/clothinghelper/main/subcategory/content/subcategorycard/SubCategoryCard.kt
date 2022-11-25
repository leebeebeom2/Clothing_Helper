package com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.title.SubCategoryCardTitle
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun SubCategoryCard(
    subCategory: () -> StableSubCategory,
    onClick: () -> Unit,
    selectedCategoryKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean,
    folders: () -> ImmutableList<StableFolder>
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
        ) {
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