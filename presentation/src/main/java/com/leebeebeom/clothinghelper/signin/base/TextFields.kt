package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextField

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    showKeyboardEnable: Boolean = false,
    @StringRes error: Int?,
    imeAction: ImeAction
) {
    MaxWidthTextField(
        label = R.string.email,
        text = email,
        onValueChange = onEmailChange,
        placeholder = R.string.email_place_holder,
        error = error,
        showKeyboardEnabled = showKeyboardEnable,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = KeyboardType.Email)
    )
}

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    @StringRes error: Int?,
    imeAction: ImeAction,
    @StringRes label: Int = R.string.password
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }

    MaxWidthTextField(
        label = label,
        text = password,
        onValueChange = onPasswordChange,
        error = error,
        trailingIcon = {
            VisibleIcon(isVisible) { isVisible = !isVisible }
        },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        )
    )
}