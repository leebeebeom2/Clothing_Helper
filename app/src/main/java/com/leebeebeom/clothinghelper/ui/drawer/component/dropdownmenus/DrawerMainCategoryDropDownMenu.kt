package com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus

import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpOffset
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.dialog.AddSubCategoryDialog
import kotlinx.collections.immutable.ImmutableSet

@Composable // skippable
fun DrawerMainCategoryDropDownMenu(
    show: () -> Boolean,
    offset: () -> DpOffset,
    onDismiss: () -> Unit,
    subCategoryNames: () -> ImmutableSet<String>,
    onAddSubCategoryPositiveClick: (name: String) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    DropdownMenu(
        expanded = show(),
        onDismissRequest = onDismiss,
        offset = offset()
    ) {
        DrawerDropdownMenuItem(
            text = R.string.add_category,
            onClick = { showDialog = true },
            onDismiss = onDismiss
        )
    }

    AddSubCategoryDialog(
        subCategoryNames = subCategoryNames,
        onPositiveButtonClick = onAddSubCategoryPositiveClick,
        show = { showDialog },
        onDismiss = { showDialog = false }
    )
}