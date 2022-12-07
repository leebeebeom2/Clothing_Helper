package com.leebeebeom.clothinghelper.main.root.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.root.components.DrawerContentRow
import com.leebeebeom.clothinghelper.main.root.components.DrawerCount
import com.leebeebeom.clothinghelper.main.root.components.DrawerExpandIcon
import com.leebeebeom.clothinghelper.main.root.components.DrawerItems
import com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus.DrawerSubCategoryDropDownMenu
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.theme.DarkGray
import com.leebeebeom.clothinghelper.util.AddFolder
import com.leebeebeom.clothinghelper.util.EditFolder
import com.leebeebeom.clothinghelper.util.EditSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerSubCategory(
    subCategory: () -> StableSubCategory,
    subCategories: () -> ImmutableList<StableSubCategory>,
    folders: (key: String) -> ImmutableList<StableFolder>,
    onClick: () -> Unit,
    onFolderClick: (StableFolder) -> Unit,
    onEditSubCategoryNamePositiveClick: EditSubCategory,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick: EditFolder
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }
    var showDropDownMenu by rememberSaveable { mutableStateOf(false) }

    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(start = 8.dp),
        onClick = onClick,
        onLongClick = { showDropDownMenu = true }
    ) {
        Row(
            Modifier
                .padding(start = 4.dp)
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SingleLineText(
                    text = subCategory().name,
                    style = MaterialTheme.typography.subtitle1
                )
                DrawerSubCategoryDropDownMenu(
                    show = { showDropDownMenu },
                    onDismiss = { showDropDownMenu = false },
                    subCategories = subCategories,
                    selectedSubCategory = subCategory,
                    onEditSubCategoryPositiveClick = onEditSubCategoryNamePositiveClick,
                    onAddFolderPositiveClick = { parentKey, subCategoryKey, name, parent ->
                        onAddFolderPositiveClick(parentKey, subCategoryKey, name, parent)
                        isExpand = true
                    },
                    folders = { folders(subCategory().key) },
                )
                SimpleHeightSpacer(dp = 2)
                DrawerCount(
                    folders = { folders(subCategory().key) }
                )
            }
            DrawerExpandIcon(
                isLoading = { false },
                isExpanded = { isExpand },
                onClick = { isExpand = !isExpand },
                items = { folders(subCategory().key) }
            )
        }
    }

    DrawerItems(
        show = { isExpand },
        items = { folders(subCategory().key) },
        background = DarkGray
    ) {
        DrawerFolder(
            folder = { it },
            onClick = onFolderClick,
            startPadding = 24.dp,
            folders = folders,
            onAddFolderPositiveClick = onAddFolderPositiveClick,
            onEditFolderPositiveClick = onEditFolderPositiveClick
        )
    }
}