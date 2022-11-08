package com.leebeebeom.clothinghelper.signin.signin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelper.signin.base.*

/*
에러 애니메이션, 로딩, 스크린 아웃, 로그인 토스트, 구글 로그인 토스트, 테스트

구글 로그인 시 최초 유저라면 유저 데이터와 최초 데이터 푸쉬
다른 계정으로 로그인 후 구글로 최초 로그인해도 전에 로그인한 유저 데이터 정상인지 확인
(구글 최초 사용자 데이터 정상인지 확인)
두 계정 번갈아 로그인해도 데이터 유지되는지 확인
 */

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SignInScreen(
    onForgotPasswordClick: () -> Unit,
    onEmailSignUpClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    state: SignInState = rememberSignInState()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center
    ) {
        EmailTextField(
            email = state.email,
            error = uiState.value.emailError,
            updateError = viewModel::updateEmailError,
            onEmailChange = state::onEmailChange
        )

        PasswordTextField(
            password = state.password,
            error = uiState.value.passwordError,
            updateError = viewModel::updatePasswordError,
            onPasswordChange = state::onPasswordChange,
            imeAction = ImeAction.Done
        )

        ForgotPasswordText(onForgotPasswordClick = onForgotPasswordClick)

        MaxWidthButton(
            state = rememberMaxWidthButtonState(
                text = R.string.sign_in,
                enabled = state.isTextNotBlank && uiState.value.isNotError
            ),
            onClick = {
                viewModel.signInWithEmailAndPassword(
                    state.email.trim(),
                    state.password.trim()
                )
            }
        )
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석 처리
        GoogleSignInButton(
            state = rememberGoogleButtonState(enabled = uiState.value.googleButtonEnabled),
            onActivityResult = viewModel::signInWithGoogleEmail
        ) { viewModel.updateGoogleButtonEnabled(enabled = false) }
        SimpleHeightSpacer(dp = 4)
        SignUpText(onEmailSignUpClick)
    }

    SimpleToast(text = uiState.value.toastText, shownToast = viewModel::toastShown)
}

@Composable
private fun SignUpText(onEmailSignUpClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.do_not_have_an_account),
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
                text = stringResource(id = R.string.forgot_password),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

data class SignInState(
    override var email: String = "",
    override var password: String = ""
) : BaseState(), EmailState, PasswordState {
    override val isTextNotBlank
        get() = email.trim().isNotBlank() && password.trim().isNotBlank()

    companion object {
        val Saver: Saver<SignInState, *> = listSaver(
            save = { listOf(it.email, it.password) },
            restore = { SignInState(it[0], it[1]) }
        )
    }
}

@Composable
fun rememberSignInState(
) = rememberSaveable(saver = SignInState.Saver) { SignInState() }