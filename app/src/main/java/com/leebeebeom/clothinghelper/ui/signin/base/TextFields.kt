package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.signin.VisibleIcon

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
    var visualTransformation: VisualTransformation by remember {
        mutableStateOf(
            PasswordVisualTransformation()
        )
    }
    val isVisible = visualTransformation == VisualTransformation.None

    MaxWidthTextField(
        label = label,
        text = password,
        onValueChange = onPasswordChange,
        error = error,
        trailingIcon = {
            VisibleIcon {
                visualTransformation =
                    if (isVisible) PasswordVisualTransformation()
                    else VisualTransformation.None
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        )
    )
}
