package com.leebeebeom.clothinghelper.main.root

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DropDownMenuRoot
import com.leebeebeom.clothinghelper.main.base.AddSubCategoryDialog
import com.leebeebeom.clothinghelper.main.subcategory.content.AddSubCategoryDialogState
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
        DropdownMenuItem(
            onClick = {
                state.showDialog()
                onDismiss()
            },
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.add_category),
                style = MaterialTheme.typography.body2
            )
        }
    }

    AddSubCategoryDialog(
        state = state,
        subCategoryNames = subCategoryNames,
        onPositiveButtonClick = onAddSubCategoryPositiveClick)
}