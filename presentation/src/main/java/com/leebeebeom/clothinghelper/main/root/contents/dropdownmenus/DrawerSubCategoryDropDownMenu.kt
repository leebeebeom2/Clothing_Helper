package com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.composables.DropDownMenuRoot
import com.leebeebeom.clothinghelper.main.base.dialogs.AddFolderDialog
import com.leebeebeom.clothinghelper.main.base.dialogs.EditSubCategoryDialog
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerSubCategoryDropDownMenu(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    subCategories: () -> ImmutableList<StableSubCategory>,
    subCategory: () -> StableSubCategory,
    onEditSubCategoryPositiveClick: (StableSubCategory) -> Unit,
    onAddFolderPositiveClick: (StableFolder) -> Unit,
    folders: () -> ImmutableList<StableFolder>
) {
    var showSubCategoryEditDialog by rememberSaveable { mutableStateOf(false) }
    var showAddFolderDialog by rememberSaveable { mutableStateOf(false) }

    DropDownMenuRoot(show = show, onDismiss = onDismiss) {
        DrawerDropdownMenuItem(
            text = R.string.edit_category,
            onClick = { showSubCategoryEditDialog = true },
            onDismiss = onDismiss
        )

        DrawerDropdownMenuItem(
            text = R.string.add_folder,
            onClick = { showAddFolderDialog = true },
            onDismiss = onDismiss
        )
    }

    EditSubCategoryDialog(show = { showSubCategoryEditDialog },
        subCategory = subCategory,
        subCategories = subCategories,
        onPositiveButtonClick = onEditSubCategoryPositiveClick,
        onDismiss = { showSubCategoryEditDialog = false })

    AddFolderDialog(folders = folders, onPositiveButtonClick = {
        onAddFolderPositiveClick(
            StableFolder(parentKey = subCategory().key, name = it, parent = subCategory().parent)
        )
    }, show = { showAddFolderDialog }, onDismiss = { showAddFolderDialog = false })
}