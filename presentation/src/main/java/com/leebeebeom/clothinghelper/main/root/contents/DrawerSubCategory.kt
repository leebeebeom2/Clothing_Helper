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
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.root.components.DrawerContentRow
import com.leebeebeom.clothinghelper.main.root.components.DrawerExpandIcon
import com.leebeebeom.clothinghelper.main.root.components.DrawerItems
import com.leebeebeom.clothinghelper.main.root.components.DrawerTotalCount
import com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus.DrawerSubCategoryDropDownMenu
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerSubCategory(
    subCategory: () -> StableSubCategory,
    onClick: () -> Unit,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onEditSubCategoryNamePositiveClick: (StableSubCategory) -> Unit,
    onAddFolderPositiveClick: (StableFolder) -> Unit,
    folders: (key: String) -> ImmutableList<StableFolder>,
    onFolderClick: (StableFolder) -> Unit
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }
    var showDropDownMenu by rememberSaveable { mutableStateOf(false) }

    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(start = 16.dp),
        onClick = onClick,
        onLongClick = { showDropDownMenu = true }
    ) {
        SingleLineText(
            modifier = Modifier.padding(start = 4.dp),
            text = subCategory().name,
            style = MaterialTheme.typography.subtitle2
        )
        DrawerSubCategoryDropDownMenu(
            show = { showDropDownMenu },
            onDismiss = { showDropDownMenu = false },
            subCategories = subCategories,
            subCategory = subCategory,
            onEditSubCategoryPositiveClick = onEditSubCategoryNamePositiveClick,
            onAddFolderPositiveClick = onAddFolderPositiveClick,
            folders = { folders(subCategory().key) },
        )
        DrawerTotalCount(items = { folders(subCategory().key) }, isLoading = { false })

        DrawerExpandIcon(
            isLoading = { false },
            isExpanded = { isExpand },
            onClick = { isExpand = !isExpand },
            items = { folders(subCategory().key) }
        )

    }

    DrawerItems(show = { isExpand }, items = { folders(subCategory().key) }, backGround = MaterialTheme.colors.primary) {
        DrawerFolder(
            folder = { it },
            onClick = { onFolderClick(it) },
            startPadding = 24.dp,
            folders = folders,
            depth = 2
        )
    }
}