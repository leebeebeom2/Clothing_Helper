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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.*

@Composable
fun SignInScreen(
    onNavigateToResetPassword: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: SignInViewModel = viewModel()
) {
    SignInColumn(
        progressOn = viewModel.progressOn,
        isFirebaseTaskFailed = viewModel.isFirebaseTaskFailed,
        progressOff = viewModel.progressOff
    ) {
        val emailTextFieldState = viewModel.emailTextFieldState

        EmailTextField(
            text = emailTextFieldState.text,
            onValueChange = emailTextFieldState.onValueChange,
            textFieldError = emailTextFieldState.error,
            isError = emailTextFieldState.isErrorEnabled,
            imeAction = ImeAction.Next
        )

        val passwordTextFieldState = viewModel.passwordTextFieldState

        PasswordTextField(
            text = passwordTextFieldState.text,
            onValueChange = passwordTextFieldState.onValueChange,
            textFieldError = passwordTextFieldState.error,
            isError = passwordTextFieldState.isErrorEnabled,
            visualTransformation = passwordTextFieldState.visualTransformation,
            imeAction = ImeAction.Done,
            visibleToggle = passwordTextFieldState.visibleToggle
        )
        SimpleHeightSpacer(dp = 4)
        ForgotPasswordText(onNavigateToResetPassword = onNavigateToResetPassword)
        SimpleHeightSpacer(dp = 8)
        FirebaseButton(
            textId = R.string.login,
            firebaseButtonEnabled = viewModel.firebaseButtonEnabled,
            onFirebaseButtonClick = viewModel.onFirebaseButtonClick
        )
        SimpleHeightSpacer(dp = 12)
        OrDivider()
        SimpleHeightSpacer(dp = 12)
        GoogleSignInButton(googleSignInImpl = viewModel)
    }

    SignUpText(onNavigateToSignUp)

    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId = R.string.login_complete)
    }

    FinishActivityOnBackPressed()
}

@Composable
private fun SignUpText(onNavigateToSignUp: () -> Unit) {
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
                modifier = Modifier.clickable(onClick = onNavigateToSignUp),
                text = stringResource(id = R.string.sign_up_with_email),
                style = MaterialTheme.typography.body2,
                color = Color(0xFF35C2C1),
            )
        }
    }
}

@Composable
private fun ForgotPasswordText(onNavigateToResetPassword: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.forget_password),
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onNavigateToResetPassword)
                .padding(4.dp)
        )
    }
}