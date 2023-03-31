package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import kotlinx.collections.immutable.ImmutableSet

@Composable // skippable
fun AddSubCategoryDialog(
    subCategoryNames: () -> ImmutableSet<String>,
    onPositiveButtonClick: (String) -> Unit,
    show: () -> Boolean,
    onDismiss: () -> Unit
) {
    if (show())
        AddDialog(
            label = R.string.category,
            placeHolder = R.string.category_place_holder,
            title = R.string.add_category,
            existNameError = R.string.error_exist_category_name,
            onPositiveButtonClick = onPositiveButtonClick,
            names = subCategoryNames,
            onDismiss = onDismiss
        )
}