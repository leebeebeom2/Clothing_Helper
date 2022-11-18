package com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DropDownMenuRoot
import com.leebeebeom.clothinghelper.base.dialogs.AddDialogState
import com.leebeebeom.clothinghelper.main.subcategory.AddSubCategoryDialog
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainCategoryDropDownMenu(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    subCategoryNames: () -> ImmutableList<String>,
    onAddSubCategoryPositiveClick: (String) -> Unit
) {
    val state = rememberSaveable(saver = AddDialogState.Saver) { AddDialogState() }

    DropDownMenuRoot(show = show, onDismiss = onDismiss) {
        DrawerDropdownMenuItem(
            text = R.string.add_category,
            onClick = state::showDialog,
            onDismiss = onDismiss
        )
    }

    AddSubCategoryDialog(
        state = state,
        subCategoryNames = subCategoryNames,
        onPositiveButtonClick = onAddSubCategoryPositiveClick
    )
}