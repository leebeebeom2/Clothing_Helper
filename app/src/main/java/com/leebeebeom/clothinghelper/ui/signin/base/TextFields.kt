package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.VisibleIcon

@Composable
fun PasswordConfirmTextField(
    passwordState: TextFieldUIState,
    onPasswordChange: (String) -> Unit,
    visualTransformationToggle: (Boolean) -> Unit
) {
    MaxWidthTextField(
        textFieldUIState = passwordState,
        onValueChange = onPasswordChange,
        labelResId = R.string.password_confirm,
        trailingIcon = { VisibleIcon(visualTransformationToggle = visualTransformationToggle) }
    )
}

@Composable
fun PasswordTextField(
    passwordState: TextFieldUIState,
    onPasswordChange: (String) -> Unit,
    visualTransformationToggle: (Boolean) -> Unit
) {
    MaxWidthTextField(
        textFieldUIState = passwordState,
        onValueChange = onPasswordChange,
        labelResId = R.string.password,
        trailingIcon = { VisibleIcon(visualTransformationToggle = visualTransformationToggle) }
    )
}

@Composable
fun EmailTextField(
    emailState: TextFieldUIState,
    onEmailChange: (String) -> Unit,
) {
    MaxWidthTextField(
        textFieldUIState = emailState,
        onValueChange = onEmailChange,
        labelResId = R.string.email,
        placeHolderResId = R.string.email_place_holder,
    )
}

@Composable
fun NameTextField(
    nameState: TextFieldUIState,
    onNameChange: (String) -> Unit,
) {
    MaxWidthTextField(
        textFieldUIState = nameState,
        onValueChange = onNameChange,
        labelResId = R.string.name,
    )
}