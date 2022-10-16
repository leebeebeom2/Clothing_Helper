package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.FinishActivityOnBackPressed
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.base.PasswordUIState
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.base.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.base.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseRoot

@Composable
fun SignInScreen(
    onForgotPasswordClick: () -> Unit,
    onEmailSignUpClick: () -> Unit,
    viewModel: SignInViewModel = viewModel()
) {
    val state = rememberSignInScreenUIState()
    val viewModelState = viewModel.viewModelState

    SignInBaseRoot(
        isLoading = viewModelState.isLoading,
        toastText = viewModelState.toastText,
        toastShown = viewModelState.toastShown
    ) {
        EmailTextField(
            email = state.email,
            error = state.emailError,
            onEmailChange = state::onEmailChange,
            imeAction = ImeAction.Next
        )

        PasswordTextField(
            password = state.password,
            onPasswordChange = state::onPasswordChange,
            error = state.passwordError,
            imeAction = ImeAction.Done
        )

        ForgotPasswordText(onForgotPasswordClick = onForgotPasswordClick)
        MaxWidthButton(
            text = R.string.login,
            enabled = state.signInButtonEnabled,
            onClick = {
                viewModel.signInWithEmailAndPassword(
                    state.email, state.password,
                    emailErrorEnabled = state::emailErrorEnabled,
                    passwordErrorEnabled = state::passwordErrorEnabled
                )
            }
        )

        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석 처리
        GoogleSignInButton(
            googleSignIn = viewModel::googleSignIn,
            googleSignInClick = viewModel::googleSignInLauncherLaunch,
            enabled = viewModelState.googleButtonEnabled
        )
    }
    SignUpText(onEmailSignUpClick)
    FinishActivityOnBackPressed()
}

@Composable
private fun SignUpText(onEmailSignUpClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dont_have_an_account),
                style = MaterialTheme.typography.body2,
            )
            TextButton(onClick = onEmailSignUpClick) {
                Text(
                    text = stringResource(id = R.string.sign_up_with_email),
                    style = MaterialTheme.typography.body2.copy(
                        color = Color(0xFF35C2C1), fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun ForgotPasswordText(onForgotPasswordClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onForgotPasswordClick
        ) {
            Text(
                text = stringResource(id = R.string.forget_password),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

class SignInScreenUIState(
    initialEmail: String = "",
    @StringRes initialEmailError: Int? = null,
    initialPassword: String = "",
    @StringRes initialPasswordError: Int? = null
) : PasswordUIState(initialEmail, initialEmailError, initialPassword, initialPasswordError) {

    val signInButtonEnabled
        get() = email.isNotBlank() && emailError == null &&
                password.isNotBlank() && passwordError == null

    companion object {
        val Saver: Saver<SignInScreenUIState, *> = listSaver(
            save = {
                listOf(
                    it.email,
                    it.emailError,
                    it.password,
                    it.passwordError
                )
            },
            restore = {
                SignInScreenUIState(
                    it[0] as String,
                    it[1] as? Int,
                    it[2] as String,
                    it[3] as? Int
                )
            }
        )
    }
}

@Composable
fun rememberSignInScreenUIState() =
    rememberSaveable(saver = SignInScreenUIState.Saver) {
        SignInScreenUIState()
    }