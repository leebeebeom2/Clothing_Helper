package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DialogRoot
import com.leebeebeom.clothinghelper.base.DialogTextButtons
import com.leebeebeom.clothinghelper.base.DialogTextField
import com.leebeebeom.clothinghelper.base.DialogTitle

@Composable
fun SubCategoryTextFieldDialog(
    onDismissDialog: () -> Unit,
    @StringRes title: Int,
    categoryName: TextFieldValue,
    error: Int?,
    onCategoryNameChange: (TextFieldValue) -> Unit,
    positiveButtonEnabled: Boolean,
    onPositiveButtonClick: () -> Unit,
    onFocusChanged: (FocusState) -> Unit
) {
    DialogRoot(onDismissDialog = onDismissDialog) {
        DialogTitle(text = title)
        DialogTextField(
            label = R.string.category,
            placeHolder = R.string.category_place_holder,
            text = categoryName,
            error = error,
            onTextChange = onCategoryNameChange,
            onFocusChanged = onFocusChanged
        )
        DialogTextButtons(
            positiveButtonEnabled = positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismissDialog = onDismissDialog
        )
    }
}