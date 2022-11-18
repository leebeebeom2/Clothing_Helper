package com.leebeebeom.clothinghelper.main.root.contents

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.root.components.DrawerContentRow
import com.leebeebeom.clothinghelper.main.root.components.DrawerContentText
import com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus.DrawerSubCategoryDropDownMenu
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerSubCategory(
    subCategory: () -> StableSubCategory,
    onClick: () -> Unit,
    subCategoryNames: () -> ImmutableList<String>,
    onEditSubCategoryNamePositiveClick: (StableSubCategory) -> Unit
) {
    var showDropDownMenu by rememberSaveable { mutableStateOf(false) }

    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(horizontal = 8.dp),
        onClick = onClick,
        onLongClick = { showDropDownMenu = true }
    ) {
        DrawerContentText(
            modifier = Modifier.padding(start = 12.dp),
            text = { subCategory().name },
            style = MaterialTheme.typography.subtitle2
        )

        DrawerSubCategoryDropDownMenu(
            show = { showDropDownMenu },
            onDismiss = { showDropDownMenu = false },
            subCategoryNames = subCategoryNames,
            subCategory = subCategory,
            onEditSubCategoryNamePositiveClick = onEditSubCategoryNamePositiveClick
        )
    }
}