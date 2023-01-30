package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.ActivityViewModel
import com.leebeebeom.clothinghelper.ui.activityViewModel
import com.leebeebeom.clothinghelper.ui.components.HeightSpacer
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute
import com.leebeebeom.clothinghelper.ui.components.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.signin.composable.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.composable.Logo
import com.leebeebeom.clothinghelper.ui.signin.composable.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.composable.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.composable.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.composable.textfield.NameTextField
import com.leebeebeom.clothinghelper.ui.signin.composable.textfield.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel

@Composable
fun SignUpScreen(
    signInNavViewModel: SignInNavViewModel,
    signInNavUiState: SignInNavUiState = signInNavViewModel.signInNavUiState,
    viewModel: SignUpViewModel = hiltViewModel(),
    uiState: SignUpUiState = viewModel.uiState,
    activityViewModel: ActivityViewModel = activityViewModel()
) {
    SignInBaseColumn {
        Logo()
        EmailTextField(error = { uiState.emailError }, onInputChange = viewModel::onEmailChange)
        NameTextField(onInputChange = viewModel::onNameChange)

        PasswordTextField(
            error = { uiState.passwordError },
            onInputChange = viewModel::onPasswordChange,
            imeActionRoute = ImeActionRoute.NEXT
        )

        PasswordTextField(
            label = R.string.password_confirm,
            error = { uiState.passwordConfirmError },
            onInputChange = viewModel::onPasswordConfirmChange
        )

        HeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.sign_up,
            enabled = { uiState.buttonEnabled },
            onClick = { viewModel.signUpWithEmailAndPassword(activityViewModel::showToast) },
        )
        OrDivider()
        GoogleSignInButton(enabled = { signInNavUiState.googleButtonEnabled },
            onActivityResult = {
                signInNavViewModel.signInWithGoogleEmail(
                    showToast = activityViewModel::showToast,
                    activityResult = it
                )
            },
            disable = { signInNavViewModel.setGoogleButtonEnable(false) })
    }
}