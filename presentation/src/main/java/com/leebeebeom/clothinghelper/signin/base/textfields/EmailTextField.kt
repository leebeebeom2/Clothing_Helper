package com.leebeebeom.clothinghelper.signin.base.textfields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.composables.rememberMaxWidthTextFieldState

@Composable
fun EmailTextField(
    email: () -> String,
    error: () -> Int?,
    imeAction: ImeAction = ImeAction.Next,
    updateError: (Int?) -> Unit,
    onEmailChange: (String) -> Unit
) {
    val state = remember { TextFieldState(email()) }

    MaxWidthTextField(
        state = rememberEmailTextFieldState(imeAction = imeAction),
        textFieldValue = { state.textFieldValue },
        error = error,
        onValueChange = {
            state.onValueChange(
                newTextFieldValue = it.copy(it.text.trim()),
                onValueChange = onEmailChange,
                updateError = updateError
            )
        },
        onFocusChanged = state::onFocusChange
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun rememberEmailTextFieldState(
    imeAction: ImeAction,
) = rememberMaxWidthTextFieldState(
    label = R.string.email,
    placeholder = R.string.email_place_holder,
    showKeyboard = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
)