package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.ActivityViewModel
import com.leebeebeom.clothinghelper.ui.activityViewModel
import com.leebeebeom.clothinghelper.ui.components.HeightSpacer
import com.leebeebeom.clothinghelper.ui.components.IconWrapper
import com.leebeebeom.clothinghelper.ui.components.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.components.SingleLineText
import com.leebeebeom.clothinghelper.ui.signin.composable.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.composable.Logo
import com.leebeebeom.clothinghelper.ui.signin.composable.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.composable.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.composable.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.composable.textfield.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.state.EmailAndPasswordState
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel

@Composable
fun SignInScreen(
    navigateToResetPassword: () -> Unit,
    navigateToSignUp: () -> Unit,
    signInNavViewModel: SignInNavViewModel,
    signInNavUiState: SignInNavUiState = signInNavViewModel.signInNavUiState,
    viewModel: SignInViewModel = viewModel(),
    uiState: EmailAndPasswordState = viewModel.uiState,
    activityViewModel: ActivityViewModel = activityViewModel()
) {
    SignInBaseColumn {
        Logo()
        EmailTextField(error = { uiState.emailError }, onInputChange = viewModel::onEmailChange)
        PasswordTextField(
            error = { uiState.passwordError }, onInputChange = viewModel::onPasswordChange
        )

        ForgotPasswordText(navigateToResetPassword = navigateToResetPassword)

        MaxWidthButton(
            text = R.string.sign_in,
            enabled = { uiState.buttonEnabled },
            onClick = { viewModel.signInWithEmailAndPassword(activityViewModel::showToast) },
        )
        OrDivider()
        MaxWidthButton(text = R.string.sign_up_with_email,
            onClick = navigateToSignUp,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
            icon = { IconWrapper(drawable = R.drawable.ic_email) })
        HeightSpacer(dp = 12)
        GoogleSignInButton(enabled = { signInNavUiState.googleButtonEnabled }, onActivityResult = {
            signInNavViewModel.signInWithGoogleEmail(
                activityResult = it, showToast = activityViewModel::showToast
            )
        }, disable = { signInNavViewModel.setGoogleButtonEnable(false) })
    }
}

@Composable
private fun ForgotPasswordText(navigateToResetPassword: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            modifier = Modifier.align(Alignment.CenterEnd), onClick = navigateToResetPassword
        ) {
            SingleLineText(
                text = R.string.forgot_password,
                style = MaterialTheme.typography.caption.copy(textDecoration = TextDecoration.Underline)
            )
        }
    }
}