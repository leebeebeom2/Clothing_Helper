package com.leebeebeom.clothinghelper.signin.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthButton
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleToast
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

//TODO 현재 키보드에 확인 버튼이 가려져서 불편함 있음
 */

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    viewModelState: SignUpViewModelState = viewModel.viewModelState
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        MaxWidthTextField(state = viewModelState.emailState)

        MaxWidthTextField(state = viewModelState.nameState)

        PasswordTextField(state = viewModelState.passwordState)

        PasswordTextField(state = viewModelState.passwordConfirmState)

        LaunchedEffect(
            key1 = viewModelState.password,
            key2 = viewModelState.passwordConfirm
        ) {
            val password = viewModelState.password
            val passwordConfirm = viewModelState.passwordConfirm

            if (password.isNotBlank() && passwordConfirm.isNotBlank() && password != passwordConfirm)
                viewModelState.passwordConfirmState.updateError(error = R.string.error_password_confirm_not_same)
            else viewModelState.passwordConfirmState.updateError(error = null)
        }

        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.sign_up,
            enabled = viewModelState.submitButtonEnable,
            onClick = viewModel::signUpWithEmailAndPassword
        )
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석처리
        GoogleSignInButton(
            signInWithGoogleEmail = viewModel::signInWithGoogleEmail,
            enabled = viewModelState.googleButtonEnabled,
            enabledOff = { viewModelState.updateGoogleButtonEnabled(false) }
        )

        SimpleHeightSpacer(dp = 150)
    }

    SimpleToast(text = viewModelState.toastText, shownToast = viewModelState::toastShown)
}