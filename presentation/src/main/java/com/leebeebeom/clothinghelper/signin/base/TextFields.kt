package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.rememberMaxWidthStateHolder

@Composable
fun EmailTextField(
    onEmailChange: (String) -> Unit,
    @StringRes error: Int?,
    imeAction: ImeAction
) {

    val state = rememberMaxWidthStateHolder(
        label = R.string.email,
        placeholder = R.string.email_place_holder,
        error = error
    )

    MaxWidthTextField(
        state = state,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = KeyboardType.Email)
    )

    val currentOnEmailChange by rememberUpdatedState(newValue = onEmailChange)
    LaunchedEffect(key1 = state) {
        snapshotFlow { state.textFiled }
            .collect { currentOnEmailChange(it.text) }
    }
}

@Composable
fun PasswordTextField(
    onPasswordChange: (String) -> Unit,
    @StringRes error: Int?,
    imeAction: ImeAction,
    @StringRes label: Int = R.string.password
) {
    val state = rememberMaxWidthStateHolder(label = label, error = error)
    var isVisible by rememberSaveable { mutableStateOf(false) }

    MaxWidthTextField(
        state = state,
        trailingIcon = { VisibleIcon(isVisible) { isVisible = !isVisible } },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        )
    )

    val currentOnPasswordChange by rememberUpdatedState(newValue = onPasswordChange)
    LaunchedEffect(key1 = state) {
        snapshotFlow { state.textFiled }.collect {
            currentOnPasswordChange(it.text)
        }
    }
}