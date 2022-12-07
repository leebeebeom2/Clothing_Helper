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
import com.leebeebeom.clothinghelper.util.AddFolder
import com.leebeebeom.clothinghelper.util.EditSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerSubCategoryDropDownMenu(
    selectedSubCategory: () -> StableSubCategory,
    subCategories: () -> ImmutableList<StableSubCategory>,
    folders: () -> ImmutableList<StableFolder>,
    show: () -> Boolean,
    onDismiss: () -> Unit,
    onEditSubCategoryPositiveClick: EditSubCategory,
    onAddFolderPositiveClick: AddFolder,
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

    EditSubCategoryDialog(
        show = { showSubCategoryEditDialog },
        selectedSubCategory = selectedSubCategory,
        subCategories = subCategories,
        onPositiveButtonClick = onEditSubCategoryPositiveClick,
        onDismiss = { showSubCategoryEditDialog = false }
    )

    AddFolderDialog(folders = folders, onPositiveButtonClick = {
        val parent = selectedSubCategory()
        onAddFolderPositiveClick(
            parent.key,
            parent.key,
            it,
            parent.parent
        )
    }, show = { showAddFolderDialog }, onDismiss = { showAddFolderDialog = false })
}