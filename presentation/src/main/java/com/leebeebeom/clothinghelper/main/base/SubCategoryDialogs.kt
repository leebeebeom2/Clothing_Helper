package com.leebeebeom.clothinghelper.main.base

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DialogRoot
import com.leebeebeom.clothinghelper.base.DialogTextButtons
import com.leebeebeom.clothinghelper.base.DialogTextField
import com.leebeebeom.clothinghelper.base.DialogTitle

@Composable
fun SubCategoryTextFieldDialog(
    onDismissDialog: () -> Unit,
    @StringRes title: Int,
    categoryName: String = "",
    error: Int? = null,
    onCategoryNameChange: (String) -> Unit,
    positiveButtonEnabled: Boolean,
    onPositiveButtonClick: () -> Unit
) {
    DialogRoot(onDismissDialog = onDismissDialog) {
        DialogTitle(text = title)
        DialogTextField(
            label = R.string.category,
            placeHolder = R.string.category_place_holder,
            text = categoryName,
            error = error,
            onCategoryNameChange = onCategoryNameChange
        )
        DialogTextButtons(
            positiveButtonEnabled = positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismissDialog = onDismissDialog
        )
    }
}