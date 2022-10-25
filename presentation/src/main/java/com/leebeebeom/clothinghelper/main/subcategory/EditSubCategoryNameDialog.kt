package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DialogRoot
import com.leebeebeom.clothinghelper.base.DialogTextButtons
import com.leebeebeom.clothinghelper.base.DialogTextField
import com.leebeebeom.clothinghelper.base.DialogTitle

@Composable
fun EditSubCategoryNameDialog(
    categoryName: String,
    @StringRes error: Int?,
    onCategoryNameChange: (String) -> Unit,
    onPositiveButtonClick: () -> Unit,
    onDisMissDialog: () -> Unit,
    positiveButtonEnabled: Boolean
) {
    DialogRoot {
        DialogTitle(title = R.string.edit_category_name)
        DialogTextField(
            categoryName = categoryName,
            error = error,
            onCategoryNameChange = onCategoryNameChange
        )
        DialogTextButtons(
            positiveButtonEnabled = positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismissDialog = onDisMissDialog
        )
    }
}