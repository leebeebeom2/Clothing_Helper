package com.leebeebeom.clothinghelper.signin.signup

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthButton
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.signin.base.*

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

//TODO 현재 키보드에 확인 버튼이 가려져서 불편함 있음
 */

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state = rememberSignUpScreenUIState()
    val viewModelState = viewModel.viewModelState

    SignInBaseRoot(
        isLoading = viewModelState.isLoading,
        toastText = viewModelState.toastText,
        toastShown = viewModelState.toastShown
    ) {
        EmailTextField(
            email = state.email,
            onEmailChange = { state.onEmailChange(it, viewModelState.hideEmailError) },
            error = viewModelState.emailError,
            imeAction = ImeAction.Next
        )

        MaxWidthTextField(
            label = R.string.name,
            text = state.name,
            onValueChange = state.onNameChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        PasswordTextField(
            password = state.password,
            onPasswordChange = state::onPasswordChange,
            error = state.passwordError,
            imeAction = ImeAction.Next
        )

        PasswordTextField(
            password = state.passwordConfirm,
            onPasswordChange = state::onPasswordConfirmChange,
            error = state.passwordConfirmError,
            imeAction = ImeAction.Done,
            label = R.string.password_confirm
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(text = R.string.sign_up,
            enabled = state.submitButtonEnabled(emailError = viewModelState.emailError),
            onClick = {
                viewModel.signUpWithEmailAndPassword(
                    email = state.email,
                    name = state.name,
                    password = state.password
                )
            })
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석처리
        GoogleSignInButton(
            signInWithGoogleEmail = viewModel::signInWithGoogleEmail,
            enabled = viewModelState.googleButtonEnabled,
            onGoogleSignInClick = viewModel.taskStart
        )
    }
}

class SignUpScreenUIState(
    email: String = "",
    name: String = "",
    password: String = "",
    passwordConfirm: String = "",
    @StringRes passwordError: Int? = null,
    @StringRes passwordConfirmError: Int? = null
) : FourTextFieldState(email, name, password, passwordConfirm) {
    val email get() = text.value
    val name get() = text2.value
    val password get() = text3.value
    val passwordConfirm get() = text4.value

    var passwordError: Int? by mutableStateOf(passwordError)
        private set
    var passwordConfirmError: Int? by mutableStateOf(passwordConfirmError)
        private set

    private val showPasswordError = { error: Int? -> this.passwordError = error }
    private val hidePasswordError = { this.passwordError = null }
    private val showPasswordConfirmError = { error: Int? -> this.passwordConfirmError = error }
    private val hidePasswordConfirmError = { this.passwordConfirmError = null }

    fun onEmailChange(email: String, hideEmailError: () -> Unit) =
        super.onTextChange(email, hideEmailError)

    val onNameChange = { name: String -> super.onText2Change(name) {} }

    fun onPasswordChange(password: String) {
        super.onText3Change(password, hidePasswordError)
        if (password.isNotBlank()) {
            passwordSameCheck()
            if (password.length < 6) showPasswordError(R.string.error_weak_password)
        }
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        super.onText4Change(passwordConfirm, hidePasswordConfirmError)
        passwordSameCheck()
    }

    private fun passwordSameCheck() {
        if (passwordConfirm.isNotBlank()) {
            if (password != passwordConfirm) showPasswordConfirmError(R.string.error_password_confirm_not_same)
            else hidePasswordConfirmError()
        }
    }

    fun submitButtonEnabled(@StringRes emailError: Int?) =
        email.isNotBlank() && emailError == null &&
                name.isNotBlank() &&
                password.isNotBlank() && passwordError == null &&
                passwordConfirm.isNotBlank() && passwordConfirmError == null

    companion object {
        val Saver: Saver<SignUpScreenUIState, *> = listSaver(save = {
            listOf(
                it.email,
                it.name,
                it.password,
                it.passwordConfirm,
                it.passwordError,
                it.passwordConfirmError
            )
        }, restore = {
            SignUpScreenUIState(
                it[0] as String,
                it[1] as String,
                it[2] as String,
                it[3] as String,
                it[4] as? Int,
                it[5] as? Int
            )
        })
    }
}

@Composable
fun rememberSignUpScreenUIState() = rememberSaveable(saver = SignUpScreenUIState.Saver) {
    SignUpScreenUIState()
}