package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.activity.result.ActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.*
import com.leebeebeom.clothinghelper.ui.signin.components.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.components.Logo
import com.leebeebeom.clothinghelper.ui.signin.components.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.components.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.PasswordTextField
import com.leebeebeom.clothinghelper.ui.util.ShowToast

const val SignInScreenTag = "sign in screen"

@Composable
fun SignInScreen(
    navigateToResetPassword: () -> Unit,
    navigateToSignUp: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    showToast: ShowToast
) {
    val state = viewModel.signInState
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignInBaseColumn(modifier = Modifier.testTag(SignInScreenTag)) {
        Logo()
        EmailTextField(
            initialEmail = state.savedEmail,
            error = { uiState.emailError },
            onEmailChange = state::setEmail
        )
        PasswordTextField(
            initialPassword = state.savedPassword,
            error = { uiState.passwordError },
            onInputChange = state::setPassword,
            imeAction = ImeAction.Done
        )

        ForgotPasswordText(navigateToResetPassword = navigateToResetPassword)

        val onSignInButtonClick: () -> Unit = remember {
            {
                viewModel.signInWithEmailAndPassword(
                    showToast = showToast
                )
            }
        }
        MaxWidthButton(
            text = R.string.sign_in,
            enabled = { uiState.buttonEnable },
            onClick = onSignInButtonClick,
        )
        OrDivider()
        MaxWidthButton(text = R.string.sign_up_with_email,
            onClick = navigateToSignUp,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
            icon = { IconWrapper(drawable = R.drawable.ic_email) })
        HeightSpacer(dp = 12)

        val onActivityResult: (ActivityResult) -> Unit = remember {
            { viewModel.signInWithGoogleEmail(activityResult = it, showToast = showToast) }
        }
        GoogleSignInButton(
            enabled = { uiState.googleButtonEnabled },
            onActivityResult = onActivityResult,
            disable = state::googleButtonDisable
        )
    }
    CenterDotProgressIndicator(show = { uiState.isLoading })
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