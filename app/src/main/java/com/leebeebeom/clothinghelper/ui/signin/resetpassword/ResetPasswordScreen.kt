package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.base.EmailUIState
import com.leebeebeom.clothinghelper.ui.signin.base.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseRoot

@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel = viewModel()) {
    val state = rememberResetScreenUIState()
    val viewModelState = viewModel.viewModelState

    if (viewModelState.goBack) {
        (LocalContext.current as ComponentActivity).onBackPressedDispatcher.onBackPressed()
        viewModelState.wentBack()
    }

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
            onEmailChange = state::onEmailChange,
            showKeyboardEnable = true,
            error = state.emailError,
            imeAction = ImeAction.Done
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.check,
            enabled = state.submitButtonEnabled,
            onClick = {
                viewModel.sendResetPasswordEmail(
                    email = state.email,
                    emailErrorEnabled = { state.emailErrorEnabled(it) })
            }
        )
    }
}

class ResetPasswordScreenUIState(
    initialEmail: String = "",
    @StringRes initialEmailError: Int? = null
) : EmailUIState(initialEmail, initialEmailError) {

    val submitButtonEnabled get() = email.isNotBlank() && emailError == null

    companion object {
        val Saver: Saver<ResetPasswordScreenUIState, *> = listSaver(
            save = { listOf(it.email, it.emailError) },
            restore = { ResetPasswordScreenUIState(it[0] as String, it[1] as? Int) }
        )
    }
}

@Composable
fun rememberResetScreenUIState() =
    rememberSaveable(saver = ResetPasswordScreenUIState.Saver) {
        ResetPasswordScreenUIState()
    }