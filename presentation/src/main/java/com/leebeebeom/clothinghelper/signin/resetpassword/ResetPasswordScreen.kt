package com.leebeebeom.clothinghelper.signin.resetpassword

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelper.signin.base.BaseState
import com.leebeebeom.clothinghelper.signin.base.EmailState
import com.leebeebeom.clothinghelper.signin.base.EmailTextFiled

/*
이메일 필드가 비어있거나 에러 메세지 표시 중일 경우 확인 버튼 비활성화
에러가 표시중인 경우 텍스트가 변경되면 에러 숨김

확인 버튼 클릭 시 로딩화면 출력, 끝나면 로딩화면 숨김

이메일 형식이 올바르지 않은 경우 "이메일 형식이 올바르지 않아요" 에러 표시
이메일을 찾을 수 없는 경우 "이메일을 찾을 수 없어요" 에러 표시

정상적으로 이메일이 발송된 경우 "이메일이 전송되었습니다" 토스트 출력
로그인 화면으로 이동

 */

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    state: ResetPasswordState = rememberResetPasswordState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isTaskSuccess) {
        (LocalContext.current as ComponentActivity).onBackPressedDispatcher.onBackPressed()
        viewModel.updateTaskSuccess(false)
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text(
            text = stringResource(id = R.string.reset_password_text),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        EmailTextFiled(
            email = state.emailState.value,
            error = uiState.emailError,
            updateError = viewModel::updateEmailError,
            onEmailChange = state::onEmailChange
        )

        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(state = rememberMaxWidthButtonState(
            text = R.string.check,
            enabled = state.isTextNotBlank && uiState.isNotError
        ),
            onClick = { viewModel.sendResetPasswordEmail(state.emailState.value.trim()) })
        SimpleHeightSpacer(dp = 80)
    }

    SimpleToast(text = uiState.toastText, shownToast = viewModel::toastShown)
}

data class ResetPasswordState(
    override val emailState: MutableState<String>
) : BaseState(), EmailState {
    override val isTextNotBlank: Boolean
        get() = emailState.value.trim().isNotBlank()
}

@Composable
fun rememberResetPasswordState(
    emailState: MutableState<String> = rememberSaveable { mutableStateOf("") }
) = remember { ResetPasswordState(emailState) }