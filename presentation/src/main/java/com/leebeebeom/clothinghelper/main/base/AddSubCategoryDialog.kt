package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryTextFieldDialog
import com.leebeebeom.clothinghelper.main.subcategory.content.AddSubCategoryDialogState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddSubCategoryDialog(
    state: AddSubCategoryDialogState = remember { AddSubCategoryDialogState() },
    subCategoryNames: () -> ImmutableList<String>,
    onPositiveButtonClick: (String) -> Unit,
) {

    SubCategoryTextFieldDialog(
        title = R.string.add_category,
        error = { state.error },
        textFieldValue = { state.textFieldValue },
        positiveButtonEnabled = { state.positiveButtonEnabled },
        showDialog = { state.showDialog },
        onValueChange = {
            state.onValueChange(it)
            if (subCategoryNames().contains(it.text.trim())) state.updateError(R.string.error_same_category_name)
        },
        onFocusChanged = state::onFocusChange,
        onDismiss = state::onDismiss,
        onPositiveButtonClick = { onPositiveButtonClick(state.text) }
    )
}