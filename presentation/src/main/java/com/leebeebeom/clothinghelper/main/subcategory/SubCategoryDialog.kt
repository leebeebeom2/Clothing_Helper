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
                placeholder = R.string.category_place_holder,
                showKeyboardEnabled = true
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

open class BaseSubCategoryDialogState(
    initialText: String,
    initialError: Int?,
) {
    var text by mutableStateOf(initialText)
        protected set

    var textFieldValue by mutableStateOf(TextFieldValue(text))
        protected set

    var error: Int? by mutableStateOf(initialError)
        protected set

    fun updateError(@StringRes error: Int?) {
        this.error = error
    }

    open val positiveButtonEnabled by derivedStateOf { text.trim().isNotBlank() && error == null }

    open fun onValueChange(newTextFiled: TextFieldValue) {
        if (text != newTextFiled.text) error = null
        text = newTextFiled.text
        textFieldValue = newTextFiled
    }
}