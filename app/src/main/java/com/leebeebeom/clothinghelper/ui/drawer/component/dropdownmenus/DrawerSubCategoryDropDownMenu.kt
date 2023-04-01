package com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus

import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.DpOffset
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.ui.component.dialog.AddFolderDialog
import com.leebeebeom.clothinghelper.ui.component.dialog.EditSubCategoryDialog
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditSubCategory
import kotlinx.collections.immutable.ImmutableSet

@Composable // skippable
fun DrawerSubCategoryDropDownMenu(
    selectedSubCategory: () -> SubCategory,
    subCategoryNames: () -> ImmutableSet<String>,
    folderNames: () -> ImmutableSet<String>,
    show: () -> Boolean,
    onDismiss: () -> Unit,
    offset: () -> DpOffset,
    onEditSubCategoryPositiveClick: EditSubCategory,
    onAddFolderPositiveClick: AddFolder,
) {
    val localSelectedSubCategory by remember { derivedStateOf(selectedSubCategory) }
    var showSubCategoryEditDialog by rememberSaveable { mutableStateOf(false) }
    var showAddFolderDialog by rememberSaveable { mutableStateOf(false) }

    DropdownMenu(
        expanded = show(),
        onDismissRequest = onDismiss,
        offset = offset()
    ) {
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
        subCategoryNames = subCategoryNames,
        onPositiveButtonClick = onEditSubCategoryPositiveClick,
        onDismiss = { showSubCategoryEditDialog = false }
    )

    AddFolderDialog(
        folderNames = folderNames,
        onPositiveButtonClick = {
            onAddFolderPositiveClick(
                localSelectedSubCategory.key,
                localSelectedSubCategory.key,
                it,
                localSelectedSubCategory.mainCategoryType
            )
        }, show = { showAddFolderDialog }, onDismiss = { showAddFolderDialog = false })
}