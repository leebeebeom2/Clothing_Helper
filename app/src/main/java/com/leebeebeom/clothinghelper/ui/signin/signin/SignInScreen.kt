package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.ClickableText
import com.leebeebeom.clothinghelper.ui.FinishActivityOnBackPressed
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.base.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.base.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseRoot

@Composable
fun SignInScreen(
    onForgotPasswordClick: () -> Unit,
    onEmailSignUpClick: () -> Unit,
    viewModel: SignInViewModel = viewModel()
) {
    val signInState = viewModel.signInState

    SignInBaseRoot(
        isLoading = signInState.isLoading,
        toastText = signInState.toastText,
        toastShown = viewModel.toastShown
    ) {
        EmailTextField(
            emailState = signInState.emailState,
            onEmailChange = viewModel.onEmailChange
        )

        PasswordTextField(
            passwordState = signInState.passwordState,
            onPasswordChange = viewModel.onPasswordChange,
            visualTransformationToggle = viewModel.passwordVisualTransformationToggle
        )
        SimpleHeightSpacer(dp = 4)
        ForgotPasswordText(onForgotPasswordClick = onForgotPasswordClick)
        SimpleHeightSpacer(dp = 4)
        MaxWidthButton(
            text = R.string.login,
            enabled = signInState.loginButtonenabled,
            onClick = viewModel::signInWithEmailAndPassword
        )

        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석 처리
        GoogleSignInButton(
            googleSignIn = viewModel::googleSignIn,
            googleSignInClick = viewModel::googleSignInLauncherLaunch,
            enabled = signInState.googleButtonEnabled
        )
    }
    SignUpText(onEmailSignUpClick)
    FinishActivityOnBackPressed()
}

@Composable
private fun SignUpText(onEmailSignUpClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.dont_have_an_account),
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(start = 4.dp)
            )
            ClickableText(
                text = R.string.sign_up_with_email,
                style = MaterialTheme.typography.body2.copy(
                    color = Color(0xFF35C2C1), fontWeight = FontWeight.Bold
                ), onClick = onEmailSignUpClick
            )
        }
    }
}

@Composable
private fun ForgotPasswordText(onForgotPasswordClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        ClickableText(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = R.string.forget_password,
            style = MaterialTheme.typography.caption,
            onClick = onForgotPasswordClick
        )
    }
}