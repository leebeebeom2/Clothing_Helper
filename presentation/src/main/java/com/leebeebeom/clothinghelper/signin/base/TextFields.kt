package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusState
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
    val textFieldValueState = remember { mutableStateOf(TextFieldValue(email)) }

    val state = rememberEmailTextFieldState(
        textFieldValueState = textFieldValueState,
        imeAction = imeAction
    )

    MaxWidthTextField(
        state = state,
        error = error,
        onValueChange = {
            onValueChange(
                newTextFieldValue = it,
                updateError = updateError,
                textFieldValueState = textFieldValueState,
                onValueChange = onEmailChange
            )
        }, onFocusChanged = { onFocusChanged(it, textFieldValueState) })
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
    val isVisibleState = rememberSaveable { mutableStateOf(false) }
    val textFieldValueState = remember { mutableStateOf(TextFieldValue(password)) }

    val state =
        rememberPasswordTextFieldState(
            label = label,
            textFieldValueState = textFieldValueState,
            imeAction = imeAction
        )

    MaxWidthTextField(
        state = state,
        error = error,
        onValueChange = {
            onValueChange(
                newTextFieldValue = it.copy(it.text.trim()), updateError = updateError,
                textFieldValueState = textFieldValueState,
                onValueChange = onPasswordChange
            )
            onPasswordChange(it.text)
        },
        onFocusChanged = {
            onFocusChanged(newFocusState = it, textFieldValueState = textFieldValueState)
        },
        trailingIcon = {
            VisibleIcon(isVisibleState.value) {
                isVisibleState.value = !isVisibleState.value
            }
        },
        visualTransformation = if (isVisibleState.value) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NameTextField(
    name: String,
    onNameChange: (String) -> Unit
) {
    val textFieldValueState = remember { mutableStateOf(TextFieldValue(name)) }

    val state = rememberMaxWidthTextFiledState(
        textFieldValueState = textFieldValueState,
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
                textFieldValueState = textFieldValueState,
                onValueChange = onNameChange
            )
        }, onFocusChanged = { onFocusChanged(it, textFieldValueState) })
}

private fun onValueChange(
    newTextFieldValue: TextFieldValue,
    updateError: (Int?) -> Unit,
    textFieldValueState: MutableState<TextFieldValue>,
    onValueChange: (String) -> Unit
) {
    if (textFieldValueState.value.text != newTextFieldValue.text) updateError(null)
    textFieldValueState.value = newTextFieldValue
    onValueChange(newTextFieldValue.text)
}

private fun onFocusChanged(
    newFocusState: FocusState,
    textFieldValueState: MutableState<TextFieldValue>
) {
    if (newFocusState.hasFocus)
        textFieldValueState.value =
            textFieldValueState.value.copy(selection = TextRange(textFieldValueState.value.text.length))
}