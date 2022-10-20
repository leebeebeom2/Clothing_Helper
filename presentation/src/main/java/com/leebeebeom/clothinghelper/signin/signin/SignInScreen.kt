package com.leebeebeom.clothinghelper.signin.signin

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthButton
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.signin.base.*

@Composable
fun SignInScreen(
    onForgotPasswordClick: () -> Unit,
    onEmailSignUpClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state = rememberSignInScreenUIState()
    val viewModelState = viewModel.viewModelState

    SignInBaseRoot(
        isLoading = viewModelState.isLoading,
        toastText = viewModelState.toastText,
        toastShown = viewModelState.toastShown
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            EmailTextField(
                email = state.email,
                error = viewModelState.emailError,
                onEmailChange = { state.onEmailChange(email = it) { viewModelState.hideEmailError() } },
                imeAction = ImeAction.Next
            )

            PasswordTextField(
                password = state.password,
                onPasswordChange = { state.onPasswordChange(password = it) { viewModelState.hidePasswordError() } },
                error = viewModelState.passwordError,
                imeAction = ImeAction.Done
            )

            ForgotPasswordText(onForgotPasswordClick = onForgotPasswordClick)

            MaxWidthButton(text = R.string.sign_in, enabled = state.signInButtonEnabled(
                emailError = viewModelState.emailError,
                passwordError = viewModelState.passwordError
            ), onClick = {
                viewModel.signInWithEmailAndPassword(
                    email = state.email, password = state.password
                )
            })
            SimpleHeightSpacer(dp = 8)
            OrDivider()
            SimpleHeightSpacer(dp = 8)
            // 프리뷰 시 주석 처리
            GoogleSignInButton(
                signInWithGoogleEmail = viewModel::signInWithGoogleEmail,
                enabled = viewModelState.googleButtonEnabled
            )
        }
        SignUpText(onEmailSignUpClick)
    }
}

@Composable
private fun SignUpText(onEmailSignUpClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
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

@Composable
private fun ForgotPasswordText(onForgotPasswordClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            modifier = Modifier.align(Alignment.CenterEnd), onClick = onForgotPasswordClick
        ) {
            Text(
                text = stringResource(id = R.string.forget_password),
                style = MaterialTheme.typography.caption
            )
        }
    }
}


class SignInScreenUIState(
    email: String = "",
    password: String = "",
) : PasswordUIState(email, password) {

    fun signInButtonEnabled(@StringRes emailError: Int?, @StringRes passwordError: Int?) =
        email.isNotBlank() && emailError == null && password.isNotBlank() && passwordError == null

    companion object {
        val Saver: Saver<SignInScreenUIState, *> = listSaver(save = {
            listOf(
                it.email, it.password
            )
        }, restore = {
            SignInScreenUIState(it[0], it[1])
        })
    }
}

@Composable
private fun rememberSignInScreenUIState() = rememberSaveable(saver = SignInScreenUIState.Saver) {
    SignInScreenUIState()
}