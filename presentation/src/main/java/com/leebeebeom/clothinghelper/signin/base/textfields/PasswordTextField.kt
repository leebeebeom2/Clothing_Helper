package com.leebeebeom.clothinghelper.signin.base.textfields

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldState
import com.leebeebeom.clothinghelper.base.rememberMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.signin.base.composables.VisibleIcon

@Composable
fun PasswordTextField(
    @StringRes label: Int = R.string.password,
    password: () -> String,
    error: () -> Int?,
    imeAction: ImeAction,
    onPasswordChange: (String) -> Unit,
    updateError: (Int?) -> Unit
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }
    val state = remember { TextFieldState(password()) }

    MaxWidthTextField(
        textFieldValue = { state.textFieldValue },
        state = rememberPasswordTextFieldState(label = label, imeAction = imeAction),
        error = error,
        onValueChange = { state.onValueChange(it, onPasswordChange, updateError) },
        onFocusChanged = state::onFocusChange,
        trailingIcon = { VisibleIcon({ isVisible }, onClick = { isVisible = !isVisible }) },
        isVisible = { isVisible },
        onCancelIconClick = { state.onCancelIconClick(onPasswordChange) }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun rememberPasswordTextFieldState(
    @StringRes label: Int = R.string.password,
    imeAction: ImeAction = ImeAction.Done
): MaxWidthTextFieldState {
    return rememberMaxWidthTextFieldState(
        label = label, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = imeAction
        )
    )
}