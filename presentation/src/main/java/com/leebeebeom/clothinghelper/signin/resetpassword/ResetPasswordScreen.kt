package com.leebeebeom.clothinghelper.signin.resetpassword

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthButton
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.signin.base.EmailTextField
import com.leebeebeom.clothinghelper.signin.base.OneTextFiledState
import com.leebeebeom.clothinghelper.signin.base.SignInBaseRoot

/*
이메일 필드가 비어있거나 에러 메세지 표시 중일 경우 확인 버튼 비활성화
에러가 표시중인 경우 텍스트가 변경되면 에러 숨김

확인 버튼 클릭 시 로딩화면 출력, 끝나면 로딩화면 숨김

이메일 형식이 올바르지 않은 경우 "이메일 형식이 올바르지 않아요" 에러 표시
이메일을 찾을 수 없는 경우 "이메일을 찾을 수 없어요" 에러 표시

정상적으로 이메일이 발송된 경우 "이메일이 전송되었습니다" 토스트 출력
로그인 화면으로 이동

//TODO 필드에 포커스 잡힐때 확인 버튼이 보이지 않아서 불편함이 있음
 */

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    popBackStack: () -> Unit
) {
    val state = rememberResetScreenUIState()
    val viewModelState = viewModel.viewModelState

    PopBackStack(viewModelState.taskSuccess, popBackStack, viewModelState::popBackStackDone)

    SignInBaseRoot(
        isLoading = viewModelState.isLoading,
        toastText = viewModelState.toastText,
        toastShown = viewModelState.toastShown
    ) {
        Text(
            text = stringResource(id = R.string.reset_password_text),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        EmailTextField(
            email = state.email,
            onEmailChange = { state.onEmailChange(it, viewModelState.hideEmailError) },
            error = viewModelState.emailError,
            imeAction = ImeAction.Done
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.check,
            enabled = state.submitButtonEnabled(viewModelState.emailError),
            onClick = { viewModel.sendResetPasswordEmail(email = state.email) }
        )
    }
}

@Composable
fun PopBackStack(
    taskSuccess: Boolean,
    popBackStack: () -> Unit,
    popBackStackDone: () -> Unit
) {
    if (taskSuccess) {
        popBackStack()
        popBackStackDone()
    }
}

class ResetPasswordScreenUIState(email: String = "") : OneTextFiledState(email) {
    val email get() = text.value

    fun onEmailChange(email: String, hideEmailError: () -> Unit) =
        super.onTextChange(email, hideEmailError)

    fun submitButtonEnabled(@StringRes emailError: Int?) =
        email.isNotBlank() && emailError == null

    companion object {
        val Saver: Saver<ResetPasswordScreenUIState, *> = listSaver(
            save = { listOf(it.email) },
            restore = { ResetPasswordScreenUIState(it[0]) }
        )
    }
}

@Composable
fun rememberResetScreenUIState() = rememberSaveable(saver = ResetPasswordScreenUIState.Saver) {
    ResetPasswordScreenUIState()
}