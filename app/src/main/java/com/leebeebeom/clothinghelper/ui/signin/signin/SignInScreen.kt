package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.FinishActivityOnBackPressed
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.VisibleIcon
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
        MaxWidthTextField(
            state = signInState.emailState,
        )

        MaxWidthTextField(
            state = signInState.passwordState,
            trailingIcon = { VisibleIcon(visualTransformationToggle = signInState.passwordState.visualTransformationToggle) }
        )

        ForgotPasswordText(onForgotPasswordClick = onForgotPasswordClick)
        MaxWidthButton(
            text = R.string.login,
            enabled = signInState.loginButtonEnabled,
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
}

@Composable
private fun ForgotPasswordText(onForgotPasswordClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onForgotPasswordClick
        ) {
            Text(
                text = stringResource(id = R.string.forget_password),
                style = MaterialTheme.typography.caption
            )
        }
    }
}