package com.leebeebeom.clothinghelper.signin.signup

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelper.signin.base.*

/*
필드가 하나라도 비어있거나 에러 메세지가 표시중인 경우 가입하기 버튼 비활성화
비밀번호, 비밀번호 확인 필드 눈 아이콘으로 가시성 토글

비밀번호 필드가 비어있지 않다면 6자 미만일때 "비밀번호는 6자리 이상이어야 합니다." 에러 메세지 표시
비밀번호 확인 필드가 비어있지 않다면 비밀번호 필드와 같지 않을때 "비밀번호가 일치하지 않습니다." 에러 메세지 표시

모든 에러 메세지는 텍스트 변경 시 숨김

모든 가입하기 시도는 로딩 화면 출력 후 로딩 화면 숨김

이메일 형식이 올바르지 않다면 "이메일 형식이 올바르지 않아요." 에러 메세지 표시
이미 사용중인 이메일일 경우 "이미 사용중인 이메일입니다." 에러 메세지 표시

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
    state: SignUpState = rememberSignUpState()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        EmailTextFiled(
            email = state.emailState.value,
            error = uiState.value.emailError,
            updateError = viewModel::updateEmailError,
            onEmailChange = state::onEmailChange
        )

        NameTextFiled(name = state.nameState.value, onNameChange = state::onNameChange)

        PasswordTextField(
            password = state.passwordState.value,
            error = state.passwordErrorState.value,
            imeAction = ImeAction.Next,
            onPasswordChange = {
                state.onPasswordChange(it)
                val newText = it.trim()
                if (newText.isNotBlank()) {
                    if (newText.length < 6)
                        state.updatePasswordError(R.string.error_weak_password)
                    if (state.passwordConfirmState.value.isNotBlank() && newText != state.passwordConfirmState.value)
                        state.updatePasswordConfirmError(R.string.error_password_confirm_not_same)
                }
            },
            updateError = state::updatePasswordError
        )

        PasswordTextField(
            label = R.string.password_confirm,
            password = state.passwordConfirmState.value,
            error = state.passwordConfirmErrorState.value,
            imeAction = ImeAction.Done,
            onPasswordChange = {
                state.onPasswordConfirmChange(it)
                if (it.isNotBlank() && state.passwordState.value.isNotBlank() && it.trim() != state.passwordState.value)
                    state.updatePasswordConfirmError(R.string.error_password_confirm_not_same)
            },
            updateError = state::updatePasswordConfirmError
        )

        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            state = rememberMaxWidthButtonState(
                text = R.string.sign_up,
                enabled = uiState.value.isNotError && state.isTextNotBlank && state.isNotError
            ),
            onClick = {
                viewModel.signUpWithEmailAndPassword(
                    email = state.emailState.value.trim(),
                    name = state.nameState.value.trim(),
                    password = state.passwordState.value.trim(),
                )
            }
        )
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석처리
        GoogleSignInButton(
            state = rememberGoogleButtonState(enabled = uiState.value.googleButtonEnabled),
            onActivityResult = viewModel::signInWithGoogleEmail
        ) { viewModel.updateGoogleButtonEnabled(enabled = false) }

        SimpleHeightSpacer(dp = 150)
    }

    SimpleToast(text = uiState.value.toastText, shownToast = viewModel::toastShown)
}

data class SignUpState(
    override val emailState: MutableState<String>,
    val nameState: MutableState<String>,
    override val passwordState: MutableState<String>,
    val passwordConfirmState: MutableState<String>,
    @StringRes val passwordErrorState: MutableState<Int?>,
    @StringRes val passwordConfirmErrorState: MutableState<Int?>
) : BaseState(), EmailState, PasswordState {

    fun onNameChange(email: String) {
        nameState.value = email.trim()
    }

    fun onPasswordConfirmChange(email: String) {
        passwordConfirmState.value = email.trim()
    }

    fun updatePasswordError(@StringRes error: Int?) {
        passwordErrorState.value = error
    }

    fun updatePasswordConfirmError(@StringRes error: Int?) {
        passwordConfirmErrorState.value = error
    }

    override val isTextNotBlank
        get() = emailState.value.trim().isNotBlank() && nameState.value.trim().isNotBlank()
                && passwordState.value.trim().isNotBlank() && passwordConfirmState.value.trim()
            .isNotBlank()

    val isNotError
        get() = passwordErrorState.value == null && passwordConfirmErrorState.value == null
}

@Composable
fun rememberSignUpState(
    emailState: MutableState<String> = rememberSaveable { mutableStateOf("") },
    nameState: MutableState<String> = rememberSaveable { mutableStateOf("") },
    passwordState: MutableState<String> = rememberSaveable { mutableStateOf("") },
    passwordConfirmState: MutableState<String> = rememberSaveable { mutableStateOf("") },
    passwordErrorState: MutableState<Int?> = rememberSaveable { mutableStateOf(null) },
    passwordConfirmErrorState: MutableState<Int?> = rememberSaveable { mutableStateOf(null) }
) = remember {
    SignUpState(
        emailState = emailState,
        nameState = nameState,
        passwordState = passwordState,
        passwordConfirmState = passwordConfirmState,
        passwordErrorState = passwordErrorState,
        passwordConfirmErrorState = passwordConfirmErrorState
    )
}