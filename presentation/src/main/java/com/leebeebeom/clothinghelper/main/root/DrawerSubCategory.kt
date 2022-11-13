package com.leebeebeom.clothinghelper.main.root

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.root.dropmenus.DrawerSubCategoryDropDownMenu
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerSubCategory(
    subCategory: () -> StableSubCategory,
    onClick: () -> Unit,
    subCategoryNames: () -> ImmutableList<String>,
    onEditSubCategoryNamePositiveClick: (String, StableSubCategory) -> Unit,
    showDropDownMenu: () -> Boolean,
    updateShowDropDownMenu: (Boolean) -> Unit
) {
    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(horizontal = 8.dp),
        onClick = onClick,
        onLongClick = { updateShowDropDownMenu(true) }
    ) {
        DrawerContentText(
            modifier = Modifier.padding(start = 12.dp),
            text = { subCategory().name },
            style = MaterialTheme.typography.subtitle2
        )

        DrawerSubCategoryDropDownMenu(
            show = showDropDownMenu,
            onDismiss = { updateShowDropDownMenu(false) },
            subCategoryNames = subCategoryNames,
            subCategory = subCategory,
            onEditSubCategoryNamePositiveClick = onEditSubCategoryNamePositiveClick
        )
    }
}