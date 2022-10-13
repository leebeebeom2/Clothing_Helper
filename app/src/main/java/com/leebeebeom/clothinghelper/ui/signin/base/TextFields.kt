package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.VisibleIcon

@Composable
fun PasswordTextField(
    passwordState: TextFieldUIState,
    onPasswordChange: (String) -> Unit,
    visualTransformationToggle: (Boolean) -> Unit,
    @StringRes label: Int = R.string.password
) {
    MaxWidthTextField(textFieldState = passwordState,
        onValueChange = onPasswordChange,
        label = label,
        trailingIcon = { VisibleIcon(visualTransformationToggle = visualTransformationToggle) })
}

@Composable
fun EmailTextField(
    emailState: TextFieldUIState,
    onEmailChange: (String) -> Unit,
    showKeyboardEnabled: Boolean = false
) {
    MaxWidthTextField(
        modifier = Modifier,
        textFieldState = emailState,
        onValueChange = onEmailChange,
        label = R.string.email,
        placeHolder = R.string.email_place_holder,
        showKeyboardEnabled = showKeyboardEnabled
    )
}

@Composable
fun NameTextField(
    nameState: TextFieldUIState,
    onNameChange: (String) -> Unit,
) {
    MaxWidthTextField(
        textFieldState = nameState,
        onValueChange = onNameChange,
        label = R.string.name,
    )
}