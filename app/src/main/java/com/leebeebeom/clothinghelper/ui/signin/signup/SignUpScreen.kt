package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.VisibleIcon
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseRoot

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    val signUpState = viewModel.signUpState

    SignInBaseRoot(
        isLoading = signUpState.isLoading,
        toastText = signUpState.toastText,
        toastShown = viewModel.toastShown
    ) {
        MaxWidthTextField(
            state = signUpState.emailState,
            showKeyboardEnabled = true
        )

        MaxWidthTextField(state = signUpState.nameState,)

        MaxWidthTextField(state = signUpState.passwordState,
            onValueChange = viewModel.onPasswordChange,
            trailingIcon = { VisibleIcon(visualTransformationToggle = signUpState.passwordState.visualTransformationToggle) })

        MaxWidthTextField(state = signUpState.passwordConfirmState,
            onValueChange = viewModel.onPasswordConfirmChange,
            trailingIcon = { VisibleIcon(visualTransformationToggle = signUpState.passwordConfirmState.visualTransformationToggle) })

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