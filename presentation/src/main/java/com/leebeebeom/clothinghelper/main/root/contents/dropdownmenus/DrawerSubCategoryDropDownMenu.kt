package com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DropDownMenuRoot
import com.leebeebeom.clothinghelper.main.subcategory.dialogs.EditSubCategoryNameDialog
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerSubCategoryDropDownMenu(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    subCategoryNames: () -> ImmutableList<String>,
    subCategory: () -> StableSubCategory,
    onEditSubCategoryNamePositiveClick: (StableSubCategory) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    DropDownMenuRoot(show = show, onDismiss = onDismiss) {
        DrawerDropdownMenuItem(
            text = R.string.edit_category_name,
            onClick = { showDialog = true },
            onDismiss = onDismiss
        )
    }

    EditSubCategoryNameDialog(
        showDialog = { showDialog },
        subCategory = subCategory,
        subCategoryNames = subCategoryNames,
        onPositiveButtonClick = onEditSubCategoryNamePositiveClick,
        onDismiss = { showDialog = false }
    )
}