package com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.composables.DropDownMenuRoot
import com.leebeebeom.clothinghelper.main.base.dialogs.AddSubCategoryDialog
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainCategoryDropDownMenu(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onAddSubCategoryPositiveClick: (String) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    DropDownMenuRoot(show = show, onDismiss = onDismiss) {
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