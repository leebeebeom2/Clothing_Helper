package com.leebeebeom.clothinghelper.signin.base.textfields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldState
import com.leebeebeom.clothinghelper.base.rememberMaxWidthTextFieldState

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
        textFieldValue = { state.textFieldValue },
        state = rememberEmailTextFieldState(imeAction = imeAction),
        error = error,
        onValueChange = { state.onValueChange(it, onEmailChange, updateError) },
        onFocusChanged = state::onFocusChange,
        onCancelIconClick = { state.onCancelIconClick(onEmailChange) }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun rememberEmailTextFieldState(
    imeAction: ImeAction,
): MaxWidthTextFieldState {
    return rememberMaxWidthTextFieldState(
        label = R.string.email,
        placeholder = R.string.email_place_holder,
        showKeyboardEnabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
    )
}