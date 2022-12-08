package com.leebeebeom.clothinghelper.ui.signin.composable.textfield

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.MaxWidthTextField
import com.leebeebeom.clothinghelper.composable.rememberMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.state.TextFieldState
import com.leebeebeom.clothinghelper.ui.signin.composable.VisibleIcon

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

    MaxWidthTextField(state = rememberPasswordTextFieldState(label = label, imeAction = imeAction),
        textFieldValue = { state.textFieldValue },
        error = error,
        onValueChange = {
            state.onValueChange(
                newTextFieldValue = it.copy(it.text.trim()),
                onValueChange = onPasswordChange,
                updateError = updateError
            )
        },
        onFocusChanged = state::onFocusChange,
        trailingIcon = { VisibleIcon({ isVisible }, onClick = { isVisible = !isVisible }) },
        isVisible = { isVisible })
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun rememberPasswordTextFieldState(@StringRes label: Int, imeAction: ImeAction) =
    rememberMaxWidthTextFieldState(
        label = label, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = imeAction
        )
    )