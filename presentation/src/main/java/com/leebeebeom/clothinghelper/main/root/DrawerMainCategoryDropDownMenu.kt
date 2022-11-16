package com.leebeebeom.clothinghelper.main.root.dropmenus

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DropDownMenuRoot
import com.leebeebeom.clothinghelper.base.SingleLineText
import com.leebeebeom.clothinghelper.main.base.AddSubCategoryDialog
import com.leebeebeom.clothinghelper.main.subcategory.EditSubCategoryNameDialog
import com.leebeebeom.clothinghelper.main.subcategory.content.AddSubCategoryDialogState
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainCategoryDropDownMenu(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    subCategoryNames: () -> ImmutableList<String>,
    onAddSubCategoryPositiveClick: (String) -> Unit
) {
    val state =
        rememberSaveable(saver = AddSubCategoryDialogState.Saver) { AddSubCategoryDialogState() }

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

@Composable
private fun DrawerDropdownMenuItem(
    @StringRes text: Int,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenuItem(
        onClick = {
            onClick()
            onDismiss()
        }, contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        SingleLineText(
            text = stringResource(id = text),
            style = MaterialTheme.typography.body2
        )
    }
}