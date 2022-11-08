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
    error: Int?,
    imeAction: ImeAction = ImeAction.Next,
    updateError: (Int?) -> Unit,
    onEmailChange: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(email)) }

    val state = rememberEmailTextFieldState(textFieldValue = textFieldValue, imeAction = imeAction)

    MaxWidthTextField(
        state = state,
        error = error,
        onValueChange = {
            onValueChange(
                newTextFieldValue = it.copy(it.text.trim()),
                updateError = updateError,
                textFieldValue = textFieldValue,
                onValueChange = { newTextFieldValue ->
                    textFieldValue = newTextFieldValue
                    onEmailChange(newTextFieldValue.text)
                }
            )
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
    error: Int?,
    imeAction: ImeAction,
    onPasswordChange: (String) -> Unit,
    updateError: (Int?) -> Unit
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(password)) }

    val state =
        rememberPasswordTextFieldState(
            label = label,
            textFieldValue = textFieldValue,
            imeAction = imeAction
        )

    MaxWidthTextField(
        state = state,
        error = error,
        onValueChange = {
            onValueChange(
                newTextFieldValue = it.copy(it.text.trim()), updateError = updateError,
                textFieldValue = textFieldValue,
                onValueChange = { newTextFieldValue ->
                    textFieldValue = newTextFieldValue
                    onPasswordChange(newTextFieldValue.text)
                }
            )
            onPasswordChange(it.text)
        },
        onFocusChanged = {
            if (it.hasFocus)
                textFieldValue =
                    textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
        },
        trailingIcon = {
            VisibleIcon(isVisible) { isVisible = !isVisible }
        },
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

    val state = rememberMaxWidthTextFiledState(
        textFieldValue = textFieldValue,
        label = R.string.name,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )

    MaxWidthTextField(
        state = state,
        error = null,
        onValueChange = {
            onValueChange(
                newTextFieldValue = it,
                updateError = {},
                textFieldValue = textFieldValue,
                onValueChange = { newTextFieldValue ->
                    textFieldValue = newTextFieldValue
                    onNameChange(newTextFieldValue.text)
                }
            )
        }, onFocusChanged = {
            if (it.hasFocus)
                textFieldValue =
                    textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
        })
}

private fun onValueChange(
    newTextFieldValue: TextFieldValue,
    updateError: (Int?) -> Unit,
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    if (textFieldValue.text != newTextFieldValue.text) updateError(null)
    onValueChange(newTextFieldValue)
}