package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.dialogs.AddDialogState
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.leebeebeom.clothinghelper.main.base.components.Fab
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BoxScope.SubCategoryFab(
    onPositiveButtonClick: (newName: String) -> Unit,
    subCategoryNames: () -> ImmutableList<String>,
    isSelectMode: () -> Boolean,
) {
    val state =
        rememberSaveable(saver = AddDialogState.Saver) { AddDialogState() }

    SubCategoryFab(isSelectMode = isSelectMode, showDialog = state::showDialog)

    AddSubCategoryDialog(state = state,
        subCategoryNames = subCategoryNames,
        onPositiveButtonClick = { onPositiveButtonClick(state.text) })
}

@Composable
private fun BoxScope.SubCategoryFab(
    showDialog: () -> Unit, isSelectMode: () -> Boolean
) {
    Fab(
        visible = { !isSelectMode() }, onClick = showDialog
    ) { SimpleIcon(drawable = R.drawable.ic_add) }
}