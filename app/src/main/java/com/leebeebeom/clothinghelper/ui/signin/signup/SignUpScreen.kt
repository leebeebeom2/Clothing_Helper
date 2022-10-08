package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.TextFieldState
import com.leebeebeom.clothinghelper.ui.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleToast
import com.leebeebeom.clothinghelper.ui.signin.*

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    SignInColumn(
        progressOn = viewModel.progressOn,
        isFirebaseTaskFailed = viewModel.isFirebaseTaskFailed,
        progressOff = viewModel.progressOff
    ) {
        val emailTextFieldState = viewModel.emailTextFieldState

        EmailTextField(
            text = emailTextFieldState.text,
            onValueChange = emailTextFieldState.onValueChange,
            textFieldError = emailTextFieldState.error,
            isError = emailTextFieldState.isErrorEnabled,
            imeAction = ImeAction.Next
        )

        val nameTextFieldState = viewModel.nameTextFieldAttr
        NameTextField(
            text = nameTextFieldState.text,
            onValueChange = nameTextFieldState.onValueChange,
            textFieldError = nameTextFieldState.error,
            isError = nameTextFieldState.isErrorEnabled,
        )

        val passwordTextFieldState = viewModel.passwordTextFieldState

        PasswordTextField(
            text = passwordTextFieldState.text,
            onValueChange = passwordTextFieldState.onValueChange,
            textFieldError = passwordTextFieldState.error,
            isError = passwordTextFieldState.isErrorEnabled,
            visualTransformation = passwordTextFieldState.visualTransformation,
            imeAction = ImeAction.Next,
            visibleToggle = passwordTextFieldState.visibleToggle
        )

        val passwordConfirmTextFieldState = viewModel.passwordConfirmTextField
        PasswordTextField(
            text = passwordConfirmTextFieldState.text,
            onValueChange = passwordConfirmTextFieldState.onValueChange,
            textFieldError = passwordConfirmTextFieldState.error,
            isError = passwordConfirmTextFieldState.isErrorEnabled,
            visualTransformation = passwordConfirmTextFieldState.visualTransformation,
            imeAction = ImeAction.Done,
            visibleToggle = passwordConfirmTextFieldState.visibleToggle
        )
        SimpleHeightSpacer(dp = 12)
        FirebaseButton(
            textId = R.string.sign_up,
            firebaseButtonEnabled = viewModel.firebaseButtonEnabled,
            onFirebaseButtonClick = viewModel.onFirebaseButtonClick
        )
        SimpleHeightSpacer(dp = 12)
        OrDivider()
        GoogleSignInButton(googleSignInImpl = viewModel)
    }

    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId = R.string.sign_up_complete)
    }
}

@Composable
fun NameTextField(
    text: String,
    onValueChange: (String) -> Unit,
    textFieldError: TextFieldState.TextFieldError,
    isError: Boolean
) {
    MaxWidthTextField(
        text = text,
        onValueChange = onValueChange,
        labelResId = R.string.name,
        textFieldError = textFieldError,
        isError = isError,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    )
}
