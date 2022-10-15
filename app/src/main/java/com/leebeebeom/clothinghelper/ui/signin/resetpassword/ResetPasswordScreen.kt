package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseRoot

@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel = viewModel()) {
    val state = viewModel.state

    if (state.goBack) {
        (LocalContext.current as ComponentActivity).onBackPressedDispatcher.onBackPressed()
        viewModel.wentBack()
    }

    SignInBaseRoot(
        isLoading = state.isLoading,
        toastText = state.toastText,
        toastShown = viewModel.toastShown
    ) {
        Text(
            text = stringResource(id = R.string.reset_password_text),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        MaxWidthTextField(
            state = state.emailState,
            showKeyboardEnabled = true
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.check,
            enabled = state.submitButtonEnabled,
            onClick = viewModel::sendResetPasswordEmail
        )
    }
}