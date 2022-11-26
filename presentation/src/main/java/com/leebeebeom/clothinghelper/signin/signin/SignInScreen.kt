package com.leebeebeom.clothinghelper.signin.signin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.*
import com.leebeebeom.clothinghelper.signin.base.composables.GoogleSignInButton
import com.leebeebeom.clothinghelper.signin.base.composables.OrDivider
import com.leebeebeom.clothinghelper.signin.base.textfields.EmailTextField
import com.leebeebeom.clothinghelper.signin.base.textfields.PasswordTextField

/*
에러 애니메이션, 로딩, 스크린 아웃, 로그인 토스트, 구글 로그인 토스트, 테스트

구글 로그인 시 최초 유저라면 유저 데이터와 최초 데이터 푸쉬
다른 계정으로 로그인 후 구글로 최초 로그인해도 전에 로그인한 유저 데이터 정상인지 확인
(구글 최초 사용자 데이터 정상인지 확인)
두 계정 번갈아 로그인해도 데이터 유지되는지 확인
 */

@Composable
fun SignInScreen(
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    uiState: SignInUIState = viewModel.uiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center
    ) {
        EmailTextField(
            email = { uiState.email },
            error = { uiState.emailError },
            updateError = uiState::updateEmailError,
            onEmailChange = uiState::onEmailChange
        )

        PasswordTextField(
            password = { uiState.password },
            error = { uiState.passwordError },
            updateError = uiState::updatePasswordError,
            onPasswordChange = uiState::onPasswordChange,
            imeAction = ImeAction.Done
        )

        ForgotPasswordText(onClick = onForgotPasswordClick)

        MaxWidthButton(
            text = R.string.sign_in,
            enabled = { uiState.buttonEnabled },
            onClick = viewModel::signInWithEmailAndPassword,
        )
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        GoogleSignInButton(
            enabled = { uiState.googleButtonEnabled },
            onActivityResult = viewModel::signInWithGoogleEmail,
            disabled = { uiState.updateGoogleButtonEnabled(false) }
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.sign_up_with_email,
            enabled = { true },
            onClick = onSignUpClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
            icon = { SimpleIcon(drawable = R.drawable.ic_email) }
        )

        SimpleToast(text = { uiState.toastText }, toastShown = uiState::toastShown)
    }
}

@Composable
private fun ForgotPasswordText(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            modifier = Modifier.align(Alignment.CenterEnd), onClick = onClick
        ) {
            SingleLineText(
                text = R.string.forgot_password,
                style = MaterialTheme.typography.caption.copy(textDecoration = TextDecoration.Underline)
            )
        }
    }
}