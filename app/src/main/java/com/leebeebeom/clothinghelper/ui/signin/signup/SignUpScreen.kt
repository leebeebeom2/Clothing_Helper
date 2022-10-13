package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.base.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.base.NameTextField
import com.leebeebeom.clothinghelper.ui.signin.base.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseRoot

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    val signUpState = viewModel.signUpState

    SignInBaseRoot(
        isLoading = signUpState.isLoading,
        toastText = signUpState.toastText,
        toastShown = viewModel.toastShown
    ) {
        EmailTextField(
            emailState = signUpState.emailState,
            onEmailChange = viewModel.onEmailChange,
            showKeyboardEnabled = true
        )

        NameTextField(
            nameState = signUpState.nameState,
            onNameChange = viewModel.onNameChange
        )

        PasswordTextField(
            passwordState = signUpState.passwordState,
            onPasswordChange = viewModel.onPasswordChange,
            visualTransformationToggle = viewModel.passwordVisualTransformationToggle
        )

        PasswordTextField(
            passwordState = signUpState.passwordConfirmState,
            onPasswordChange = viewModel.onPasswordConfirmChange,
            visualTransformationToggle = viewModel.passwordConfirmVisualTransformationToggle,
            label = R.string.password_confirm
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.sign_up,
            enabled = signUpState.signUpButtonEnabled,
            onClick = viewModel::signUpWithEmailAndPassword
        )
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석 처리
        GoogleSignInButton(
            googleSignInClick = viewModel::googleSignInLauncherLaunch,
            googleSignIn = viewModel::googleSignIn,
            enabled = signUpState.googleButtonEnabled
        )
    }
}