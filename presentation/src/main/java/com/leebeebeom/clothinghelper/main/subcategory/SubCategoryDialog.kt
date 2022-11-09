package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SubCategoryTextFieldDialog(
    @StringRes title: Int,
    @StringRes error: () -> Int?,
    textFieldValue: () -> TextFieldValue,
    positiveButtonEnabled: () -> Boolean,
    showDialog: () -> Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog())
        DialogRoot(onDismiss = onDismiss) {
            DialogTitle(text = title)

            val textFieldState = rememberMaxWidthTextFiledState(
                label = R.string.category,
                showKeyboardEnabled = true,
                placeholder = R.string.category_place_holder
            )

            MaxWidthTextField(
                textFieldValue = textFieldValue,
                state = textFieldState,
                error = error,
                onValueChange = onValueChange,
                onFocusChanged = onFocusChanged
            )
            SimpleHeightSpacer(dp = 12)

            DialogTextButtons(
                positiveButtonEnabled = positiveButtonEnabled,
                onPositiveButtonClick = onPositiveButtonClick,
                onDismiss = onDismiss
            )
        }
}