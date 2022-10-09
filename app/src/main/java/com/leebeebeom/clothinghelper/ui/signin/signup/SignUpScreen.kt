package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.OrDivider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.ui.signin.base.*

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    val signUpState = viewModel.signUpState

    SignInBaseRoot(
        isLoading = signUpState.isLoading,
        toastTextId = signUpState.toastTextId,
        toastShown = viewModel::toastShown
    ) {
        EmailTextField(
            emailState = signUpState.emailState,
            onEmailChange = viewModel.onEmailChange
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

        PasswordConfirmTextField(
            passwordState = signUpState.passwordConfirmState,
            onPasswordChange = viewModel.onPasswordConfirmChange,
            visualTransformationToggle = viewModel.passwordConfirmVisualTransformationToggle
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            text = stringResource(id = R.string.sign_up),
            enabled = signUpState.signUpButtonEnable,
            onClick = viewModel::signUpWithEmailAndPassword
        )
        SimpleHeightSpacer(dp = 12)
        OrDivider()
        GoogleSignInButton(
            googleSignInClick = viewModel::googleSignInLauncherLaunch,
            googleSignIn = viewModel::googleSignIn,
            gso = viewModel.getGso(stringResource(id = R.string.default_web_client_id))
        )
    }
}