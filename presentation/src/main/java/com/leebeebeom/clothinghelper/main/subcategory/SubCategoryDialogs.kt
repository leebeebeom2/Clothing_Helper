package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SubCategoryTextFieldDialog(
    @StringRes titleRes: Int,
    error: Int?,
    textFieldValueState: State<TextFieldValue>,
    positiveButtonEnabled: Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {

    val maxWidthTextFieldState =
        rememberMaxWidthTextFiledState(
            textFieldValueState = textFieldValueState,
            label = R.string.category,
            showKeyboardEnabled = true,
            placeholder = R.string.category_place_holder
        )

    DialogRoot(onDismiss = onDismiss) {
        DialogTitle(text = titleRes)

        DialogTextField(
            state = maxWidthTextFieldState.value,
            error = error,
            onValueChange = onValueChange,
            onFocusChanged = onFocusChanged
        )
        DialogTextButtons(
            positiveButtonEnabled = positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismiss = onDismiss
        )
    }
}