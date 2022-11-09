package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.rememberEmailTextFieldState
import com.leebeebeom.clothinghelper.base.rememberMaxWidthTextFiledState
import com.leebeebeom.clothinghelper.base.rememberPasswordTextFieldState

@Composable
fun EmailTextField(
    email: String,
    error: () -> Int?,
    imeAction: ImeAction = ImeAction.Next,
    updateError: (Int?) -> Unit,
    onEmailChange: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(email)) }

    MaxWidthTextField(
        textFieldValue = { textFieldValue },
        state = rememberEmailTextFieldState(imeAction = imeAction),
        error = error,
        onValueChange = {
            if (textFieldValue.text != it.text) updateError(null)
            textFieldValue = it.copy(it.text.trim())
            onEmailChange(textFieldValue.text)
        }, onFocusChanged = {
            if (it.hasFocus)
                textFieldValue =
                    textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
        })
}

@Composable
fun PasswordTextField(
    @StringRes label: Int = R.string.password,
    password: String,
    error: () -> Int?,
    imeAction: ImeAction,
    onPasswordChange: (String) -> Unit,
    updateError: (Int?) -> Unit
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(password)) }

    MaxWidthTextField(
        textFieldValue = { textFieldValue },
        state = rememberPasswordTextFieldState(label = label, imeAction = imeAction),
        error = error,
        onValueChange = {
            if (textFieldValue.text != it.text) updateError(null)
            textFieldValue = it.copy(it.text.trim())
            onPasswordChange(textFieldValue.text)
        },
        onFocusChanged = {
            if (it.hasFocus)
                textFieldValue =
                    textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
        },
        trailingIcon = { VisibleIcon({ isVisible }) { isVisible = !isVisible } },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NameTextField(
    name: String,
    onNameChange: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(name)) }

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
        }, onFocusChanged = {
            if (it.hasFocus)
                textFieldValue =
                    textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
        })
}