package com.leebeebeom.clothinghelper.main.subcategory.dialogs

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.dialogs.AddDialog
import com.leebeebeom.clothinghelper.base.dialogs.AddDialogState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddSubCategoryDialog(
    state: AddDialogState,
    subCategoryNames: () -> ImmutableList<String>,
    onPositiveButtonClick: (String) -> Unit,
) {
    AddDialog(
        label = R.string.add_category,
        placeHolder = R.string.category_place_holder,
        title = R.string.add_category,
        state = state,
        names = subCategoryNames,
        existNameError = R.string.error_exist_category_name,
        onPositiveButtonClick = onPositiveButtonClick
    )
}