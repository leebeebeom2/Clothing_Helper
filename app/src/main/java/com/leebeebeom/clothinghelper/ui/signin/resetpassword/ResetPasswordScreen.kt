package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleToast
import com.leebeebeom.clothinghelper.ui.signin.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.FirebaseButton
import com.leebeebeom.clothinghelper.ui.signin.SignInColumn

@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel = viewModel(), popBackStack: () -> Unit) {
    SignInColumn(
        progressOn = viewModel.progressOn,
        isFirebaseTaskFailed = viewModel.isFirebaseTaskFailed,
        progressOff = viewModel.progressOff
    ) {
        ResetPasswordHeader()
        SimpleHeightSpacer(dp = 4)

        val emailTextFieldState = viewModel.emailTextFieldState

        EmailTextField(
            text = emailTextFieldState.text,
            onValueChange = emailTextFieldState.onValueChange,
            textFieldError = emailTextFieldState.error,
            isError = emailTextFieldState.isErrorEnabled,
            imeAction = ImeAction.Done
        )
        SimpleHeightSpacer(dp = 12)
        FirebaseButton(
            textId = R.string.check,
            firebaseButtonEnabled = viewModel.firebaseButtonEnabled,
            onFirebaseButtonClick = viewModel.onFirebaseButtonClick
        )
    }

    if (viewModel.isFirebaseTaskSuccessful) {
        SimpleToast(resId = R.string.email_send_complete)
        popBackStack()
        viewModel.isFirebaseTaskSuccessful = false
    }
}

@Composable
private fun ResetPasswordHeader() {
    Text(
        text = stringResource(id = R.string.reset_password_text),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(start = 4.dp)
    )
}