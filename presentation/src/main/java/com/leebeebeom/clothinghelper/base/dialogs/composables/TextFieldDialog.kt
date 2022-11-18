package com.leebeebeom.clothinghelper.base.dialogs.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.rememberMaxWidthTextFieldState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldDialog(
    @StringRes label: Int,
    @StringRes placeHolder: Int,
    @StringRes title: Int,
    @StringRes error: () -> Int?,
    textFieldValue: () -> TextFieldValue,
    positiveButtonEnabled: () -> Boolean,
    showDialog: () -> Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    onCancelIconClick: () -> Unit
) {
    if (showDialog())
        DialogRoot(onDismiss = onDismiss) {
            DialogTitle(text = title)

            val state = rememberMaxWidthTextFieldState(
                label = label, placeholder = placeHolder, showKeyboardEnabled = true
            )

            MaxWidthTextField(
                textFieldValue = textFieldValue,
                state = state,
                error = error,
                onValueChange = onValueChange,
                onFocusChanged = onFocusChanged,
                onCancelIconClick = onCancelIconClick
            )
            SimpleHeightSpacer(dp = 12)

            DialogTextButtons(
                positiveButtonEnabled = positiveButtonEnabled,
                onPositiveButtonClick = onPositiveButtonClick,
                onDismiss = onDismiss
            )
        }
}

abstract class BaseTextFieldDialogState(
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

    fun initTextFieldValue() {
        textFieldValue = textFieldValue.copy("")
    }
}