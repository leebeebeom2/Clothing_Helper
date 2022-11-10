package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.rememberEmailTextFieldState
import com.leebeebeom.clothinghelper.base.rememberMaxWidthTextFiledState
import com.leebeebeom.clothinghelper.base.rememberPasswordTextFieldState

@Composable
fun EmailTextField(
    email: () -> String,
    error: () -> Int?,
    imeAction: ImeAction = ImeAction.Next,
    updateError: (Int?) -> Unit,
    onEmailChange: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(email())) }
    val onValueChange = rememberOnValueChange(
        textFieldValue = textFieldValue,
        updateError = updateError,
        updateTextFieldValue = { textFieldValue = it },
        onValueChange = onEmailChange
    )
    val onFocusChange = rememberOnFocusChange(
        textFieldValue = textFieldValue,
        updateTextFieldValue = { textFieldValue = it }
    )

    MaxWidthTextField(
        textFieldValue = { textFieldValue },
        state = rememberEmailTextFieldState(imeAction = imeAction),
        error = error,
        onValueChange = onValueChange,
        onFocusChanged = onFocusChange
    )
}

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
    val onVisibleIconClick = remember { { isVisible = !isVisible } }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(password())) }
    val onValueChange = rememberOnValueChange(
        textFieldValue = textFieldValue,
        updateError = updateError,
        updateTextFieldValue = { textFieldValue = it },
        onValueChange = onPasswordChange
    )
    val onFocusChange = rememberOnFocusChange(
        textFieldValue = textFieldValue,
        updateTextFieldValue = { textFieldValue = it }
    )

    MaxWidthTextField(
        textFieldValue = { textFieldValue },
        state = rememberPasswordTextFieldState(label = label, imeAction = imeAction),
        error = error,
        onValueChange = onValueChange,
        onFocusChanged = onFocusChange,
        trailingIcon = { VisibleIcon({ isVisible }, onClick = onVisibleIconClick) },
        isVisible = { isVisible }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NameTextField(
    name: () -> String,
    onNameChange: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(name())) }
    val onFocusChange = rememberOnFocusChange(
        textFieldValue = textFieldValue,
        updateTextFieldValue = { textFieldValue = it }
    )

    MaxWidthTextField(
        textFieldValue = { textFieldValue },
        state = rememberMaxWidthTextFiledState(
            label = R.string.name,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        ),
        error = { null },
        onValueChange = {
            textFieldValue = it
            onNameChange(it.text)
        }, onFocusChanged = onFocusChange)
}

@Composable
fun rememberOnValueChange(
    textFieldValue: TextFieldValue,
    updateError: (Int?) -> Unit,
    updateTextFieldValue: (TextFieldValue) -> Unit,
    onValueChange: (String) -> Unit
): (TextFieldValue) -> Unit {
    return remember {
        {
            if (textFieldValue.text != it.text) updateError(null)
            updateTextFieldValue(it.copy(it.text.trim()))
            onValueChange(textFieldValue.text)
        }
    }
}

@Composable
fun rememberOnFocusChange(
    textFieldValue: TextFieldValue,
    updateTextFieldValue: (TextFieldValue) -> Unit
): (FocusState) -> Unit {
    return remember {
        {
            if (it.hasFocus) updateTextFieldValue(
                textFieldValue.copy(
                    selection = TextRange(
                        textFieldValue.text.length
                    )
                )
            )
        }
    }
}