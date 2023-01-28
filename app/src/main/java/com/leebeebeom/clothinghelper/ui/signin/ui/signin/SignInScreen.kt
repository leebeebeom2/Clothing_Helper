package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.ActivityViewModel
import com.leebeebeom.clothinghelper.ui.components.HeightSpacer
import com.leebeebeom.clothinghelper.ui.components.IconWrapper
import com.leebeebeom.clothinghelper.ui.components.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.components.SingleLineText
import com.leebeebeom.clothinghelper.ui.getActivityViewModel
import com.leebeebeom.clothinghelper.ui.signin.composable.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.composable.Logo
import com.leebeebeom.clothinghelper.ui.signin.composable.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.composable.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.composable.textfield.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.state.EmailAndPasswordState
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel

/*
에러 애니메이션, 로딩, 스크린 아웃, 로그인 토스트, 구글 로그인 토스트, 테스트

구글 로그인 시 최초 유저라면 유저 데이터와 최초 데이터 푸쉬
다른 계정으로 로그인 후 구글로 최초 로그인해도 전에 로그인한 유저 데이터 정상인지 확인
(구글 최초 사용자 데이터 정상인지 확인)
두 계정 번갈아 로그인해도 데이터 유지되는지 확인
 */

@Composable
fun SignInScreen(
    navigateToResetPassword: () -> Unit,
    navigateToSignUp: () -> Unit,
    signInNavViewModel: SignInNavViewModel,
    signInNavUiState: SignInNavUiState = signInNavViewModel.signInNavUiState,
    viewModel: SignInViewModel = viewModel(),
    uiState: EmailAndPasswordState = viewModel.uiState,
    activityViewModel: ActivityViewModel = getActivityViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 40.dp), verticalArrangement = Arrangement.Center
    ) {
        Logo()
        EmailTextField(
            error = { uiState.emailError }, onInputChange = viewModel::onEmailChange
        )

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
        GoogleSignInButton(enabled = { signInNavUiState.googleButtonEnabled },
            onActivityResult = {
                signInNavViewModel.signInWithGoogleEmail(
                    activityResult = it,
                    showToast = activityViewModel::showToast
                )
            },
            onClick = { signInNavViewModel.setGoogleButtonEnable(false) })
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