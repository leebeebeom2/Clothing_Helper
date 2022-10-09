package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.*
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
        toastTextId = signInState.toastTextId,
        toastShown = viewModel::toastShown
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
        ForgotPasswordText(onForgotPasswordClick = onForgotPasswordClick)
        SimpleHeightSpacer(dp = 8)
        MaxWidthButton(
            text = stringResource(id = R.string.login),
            enabled = signInState.loginButtonEnable,
            onClick = { viewModel.signInWithEmailAndPassword() }
        )

        SimpleHeightSpacer(dp = 12)
        OrDivider()
        SimpleHeightSpacer(dp = 12)
        // TODO 나중에 테스트 필요
        GoogleSignInButton(
            gso = viewModel.getGso(stringResource(id = R.string.default_web_client_id)),
            googleSignIn = viewModel::googleSignIn,
            googleSignInClick = viewModel::googleSignInLauncherLaunch
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
                style = MaterialTheme.typography.body2
            )
            SimpleWidthSpacer(dp = 6)
            Text(
                modifier = Modifier.clickable(onClick = onEmailSignUpClick),
                text = stringResource(id = R.string.sign_up_with_email),
                style = MaterialTheme.typography.body2,
                color = Color(0xFF35C2C1),
            )
        }
    }
}

@Composable
private fun ForgotPasswordText(onForgotPasswordClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.forget_password),
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onForgotPasswordClick)
                .padding(4.dp)
        )
    }
}