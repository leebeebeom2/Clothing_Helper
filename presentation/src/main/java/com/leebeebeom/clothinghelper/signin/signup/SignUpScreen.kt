package com.leebeebeom.clothinghelper.signin.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelper.signin.base.BaseState
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInButton
import com.leebeebeom.clothinghelper.signin.base.OrDivider
import com.leebeebeom.clothinghelper.signin.base.PasswordTextField

/*
필드가 하나라도 비어있거나 에러 메세지가 표시중인 경우 가입하기 버튼 비활성화
비밀번호, 비밀번호 확인 필드 눈 아이콘으로 가시성 토글

비밀번호 필드가 비어있지 않다면 6자 미만일때 "비밀번호는 6자리 이상이어야 합니다." 에러 메세지 표시
비밀번호 확인 필드가 비어있지 않다면 비밀번호 필드와 같지 않을때 "비밀번호가 일치하지 않습니다." 에러 메세지 표시

이메일 형식이 올바르지 않다면 "이메일 형식이 올바르지 않아요." 에러 메세지 표시
이미 사용중인 이메일일 경우 "이미 사용중인 이메일입니다." 에러 메세지 표시

모든 가입하기 시도는 로딩 화면 출력


가입하기 성공이라면 로딩 화면 사라지고 가입 화면이 밑으로 사라지며 메인 화면 노출
"회원 가입이 완료되었습니다." 토스트 출력

구글 로그인 시 구글 로그인 버튼이 비활성화되며 로딩 화면 표시
로그인 성공 시 로딩 화면이 사라지고 화면이 밑으로 내려가며 메인 화면 노출
"구글 이메일로 로그인 되었습니다." 토스트 출력

가입 시 초기 데이터 푸쉬 여부 확인

구글 로그인 시 최초 유저라면 유저 데이터와 최초 데이터 푸쉬
다른 계정으로 로그인 후 구글로 최초 로그인해도 전에 로그인한 유저 데이터 정상인지 확인
(구글 최초 사용자 데이터 정상인지 확인)

 */

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    signUpScreenState: SignUpScreenState = rememberSignUpScreenState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        MaxWidthTextField(
            maxWidthTextFieldState = signUpScreenState.email,
            error = uiState.emailError,
            onValueChange = {
                signUpScreenState.email.onValueChange(it, viewModel::updateEmailError)
            },
            onFocusChanged = signUpScreenState.email::onFocusChanged
        )

        MaxWidthTextField(
            maxWidthTextFieldState = signUpScreenState.name,
            onValueChange = { signUpScreenState.name.onValueChange(it) {} },
        )

        PasswordTextField(
            maxWidthTextFieldState = signUpScreenState.password,
            error = uiState.passwordError,
            onValueChange = {
                signUpScreenState.password.onValueChange(it, viewModel::updatePasswordError)
                val newText = it.text.trim()
                if (newText.isNotBlank() && newText.length < 6)
                    viewModel.updatePasswordError(R.string.error_weak_password)
            }
        )

        PasswordTextField(
            maxWidthTextFieldState = signUpScreenState.passwordConfirm,
            error = uiState.passwordConfirmError,
            onValueChange = {
                signUpScreenState.passwordConfirm.onValueChange(
                    it,
                    viewModel::updatePasswordConfirmError
                )
                val password = signUpScreenState.password.text.trim()
                val passwordConfirm = signUpScreenState.passwordConfirm.text.trim()
                if (password.isNotBlank() && passwordConfirm.isNotBlank() && password != passwordConfirm)
                    viewModel.updatePasswordConfirmError(R.string.error_password_confirm_not_same)
                else viewModel.updatePasswordConfirmError(null)
            }
        )

        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            maxWidthButtonState = signUpScreenState.signUpButton,
            enabled = uiState.isNotError && signUpScreenState.isTextNotBlank,
            onClick = {
                viewModel.signUpWithEmailAndPassword(
                    email = signUpScreenState.email.text.trim(),
                    name = signUpScreenState.name.text.trim(),
                    password = signUpScreenState.password.text.trim(),
                )
            }
        )
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석처리
        GoogleSignInButton(
            maxWidthButtonState = signUpScreenState.googleButtonState,
            signInWithGoogleEmail = viewModel::signInWithGoogleEmail,
            enabled = uiState.googleButtonEnabled,
            enabledOff = { viewModel.updateGoogleButtonEnabled(enabled = false) }
        )

        SimpleHeightSpacer(dp = 150)
    }

    SimpleToast(text = uiState.toastText, shownToast = viewModel::toastShown)
}

data class SignUpScreenState(
    val email: MaxWidthTextFieldState,
    val name: MaxWidthTextFieldState,
    val password: MaxWidthTextFieldState,
    val passwordConfirm: MaxWidthTextFieldState,
    val signUpButton: MaxWidthButtonState,
    val googleButtonState: MaxWidthButtonState
) : BaseState() {
    override val isTextNotBlank: Boolean
        get() = email.text.trim().isNotBlank() && name.text.trim().isNotBlank()
                && password.text.trim().isNotBlank() && passwordConfirm.text.trim().isNotBlank()
}

@Composable
fun rememberSignUpScreenState(
    email: MaxWidthTextFieldState = rememberEmailTextFieldState(imeAction = ImeAction.Next),
    name: MaxWidthTextFieldState = rememberMaxWidthTextFiledState(
        label = R.string.name,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    ),
    password: MaxWidthTextFieldState = rememberPasswordTextFieldState(imeAction = ImeAction.Next),
    passwordConfirm: MaxWidthTextFieldState = rememberPasswordTextFieldState(
        label = R.string.password_confirm,
        imeAction = ImeAction.Done
    ),
    signUpButton: MaxWidthButtonState = rememberMaxWidthButtonState(R.string.sign_up),
    googleButtonState: MaxWidthButtonState = rememberGoogleButtonState()
) = remember {
    SignUpScreenState(
        email = email,
        name = name,
        password = password,
        passwordConfirm = passwordConfirm,
        signUpButton = signUpButton, googleButtonState = googleButtonState
    )
}