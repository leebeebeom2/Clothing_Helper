package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.input.*
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextField

@Composable
fun EmailTextField(
    email: TextFieldValue,
    onEmailChange: (TextFieldValue) -> Unit,
    @StringRes error: Int?,
    imeAction: ImeAction,
    onFocusChanged:(FocusState) -> Unit
) {
    MaxWidthTextField(
        label = R.string.email,
        text = email,
        onValueChange = onEmailChange,
        placeholder = R.string.email_place_holder,
        error = error,
        showKeyboardEnabled = false,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = KeyboardType.Email),
        onFocusChanged = onFocusChanged
    )
}

@Composable
fun PasswordTextField(
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    @StringRes error: Int?,
    imeAction: ImeAction,
    @StringRes label: Int = R.string.password,
    onFocusChanged:(FocusState) -> Unit
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }

    MaxWidthTextField(
        label = label,
        text = password,
        onValueChange = onPasswordChange,
        error = error,
        trailingIcon = { VisibleIcon(isVisible) { isVisible = !isVisible } },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        ),
        onFocusChanged = onFocusChanged
    )
}