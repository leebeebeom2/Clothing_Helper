package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import kotlinx.collections.immutable.ImmutableSet

@Composable // skippable
fun EditSubCategoryDialog(
    show: () -> Boolean,
    initialName: () -> String,
    subCategoryNames: () -> ImmutableSet<String>,
    onPositiveButtonClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (show()) EditNameDialog(
        label = R.string.category,
        placeHolder = R.string.category_place_holder,
        title = R.string.edit_category,
        existNameError = R.string.error_exist_category_name,
        onDismiss = onDismiss,
        onPositiveButtonClick = onPositiveButtonClick,
        initialName = initialName(),
        names = subCategoryNames
    )
}