package com.leebeebeom.clothinghelper.signin.signin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.leebeebeom.clothinghelper.signin.base.BaseStateHolder
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInButton
import com.leebeebeom.clothinghelper.signin.base.OrDivider
import com.leebeebeom.clothinghelper.signin.base.PasswordTextField

/*
텍스트 필드가 하나라도 비어있을 경우 로그인 버튼 비 활성화

비밀번호 필드 숨김 처리
눈 아이콘으로 가시성 토글

모든 로그인 시도 시 로딩화면 표시
실패 혹은 성공 시 로딩화면 숨김

이메일 형식이 올바르지 않을 경우 에러 메세지 "이메일 형식이 올바르지 않아요"
이메일을 찾을 수 없을 경우 에러 메세지 "이메일을 찾을 수 없어요."
비밀번호가 맞지 않을 경우 에러 메세지 "비밀번호를 확인해 주세요"

에러 메세지는 텍스트가 변경될 경우 사라짐
에러 메세지가 있을 경우 로그인 버튼 비 활성화

이메일과 패스워드가 일치할 경우 로딩화면이 숨겨지며 로그인
이때 로그인 화면은 화면 아래로 이동하면서 사라짐

구글 로그인 시 구글 로그인 버튼이 비활성화되며 로딩 화면 표시
로그인 성공 시 로딩 화면이 사라지고 화면이 밑으로 내려가며 메인 화면 노출

로그인 성공 시 "로그인 되었습니다." 토스트 출력
구글 로그인 성공 시 "구글 이메일로 로그인 되었습니다." 토스트 출력

"비밀 번호를 잊으셨나요?" 텍스트 클릭 시 비밀번호 재설정 화면으로 이동
"이메일로 가입하기" 텍스트 클릭 시 회원가입 화면으로 이동

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
    stateHolder: SignInStateHolder = rememberSignInStateHolder()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center
    ) {
        MaxWidthTextField(
            stateHolder = stateHolder.emailStateHolder,
            error = uiState.emailError,
            onValueChange = {
                stateHolder.emailStateHolder.onValueChange(it, viewModel::updateEmailError)
            }
        )

        PasswordTextField(
            maxWidthTextFieldStateHolder = stateHolder.passwordStateHolder,
            error = uiState.passwordError,
            onValueChange = {
                stateHolder.passwordStateHolder.onValueChange(it, viewModel::updatePasswordError)
            }
        )

        ForgotPasswordText(onForgotPasswordClick = onForgotPasswordClick)

        MaxWidthButton(
            maxWidthButtonStateHolder = stateHolder.singInButtonStateHolder,
            enabledState = stateHolder.isTextNotBlank && uiState.isNotError,
            onClick = {
                viewModel.signInWithEmailAndPassword(
                    stateHolder.emailStateHolder.textState.trim(),
                    stateHolder.passwordStateHolder.textState.trim()
                )
            }
        )
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석 처리
        GoogleSignInButton(
            maxWidthButtonStateHolder = stateHolder.googleButtonStateHolder,
            signInWithGoogleEmail = viewModel::signInWithGoogleEmail,
            enabled = uiState.googleButtonEnabled,
            enabledOff = { viewModel.updateGoogleButtonEnabled(enabled = false) }
        )
        SimpleHeightSpacer(dp = 4)
        SignUpText(onEmailSignUpClick)
    }

    SimpleToast(text = uiState.toastText, shownToast = viewModel::toastShown)
}

@Composable
private fun SignUpText(onEmailSignUpClick: () -> Unit) {
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

data class SignInStateHolder(
    val emailStateHolder: MaxWidthTextFieldStateHolder,
    val passwordStateHolder: MaxWidthTextFieldStateHolder,
    val singInButtonStateHolder: MaxWidthButtonStateHolder,
    val googleButtonStateHolder: MaxWidthButtonStateHolder
) : BaseStateHolder() {
    override val isTextNotBlank
        get() = emailStateHolder.textState.trim().isNotBlank() && passwordStateHolder.textState.trim().isNotBlank()
}

@Composable
fun rememberSignInStateHolder(
    email: MaxWidthTextFieldStateHolder = rememberEmailTextFieldStateHolder(imeAction = ImeAction.Next),
    password: MaxWidthTextFieldStateHolder = rememberPasswordTextFieldStateHolder(imeAction = ImeAction.Done),
    signInButtonState: MaxWidthButtonStateHolder = rememberMaxWidthButtonStateHolder(text = R.string.sign_in),
    googleButtonState: MaxWidthButtonStateHolder = rememberGoogleButtonStateHolder()
) = remember {
    SignInStateHolder(
        emailStateHolder = email,
        passwordStateHolder = password,
        singInButtonStateHolder = signInButtonState,
        googleButtonStateHolder = googleButtonState
    )
}