package com.leebeebeom.clothinghelper.ui.main.root.contents.dropdownmenus

import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.ui.main.dialogs.AddSubCategoryDialog
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainCategoryDropDownMenu(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onAddSubCategoryPositiveClick: (name: String) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    DropdownMenu(expanded = show(), onDismissRequest = onDismiss) {
        DrawerDropdownMenuItem(
            text = R.string.add_category,
            onClick = { showDialog = true },
            onDismiss = onDismiss
        )
    }

    AddSubCategoryDialog(
        subCategories = subCategories,
        onPositiveButtonClick = onAddSubCategoryPositiveClick,
        show = { showDialog },
        onDismiss = { showDialog = false }
    )
}