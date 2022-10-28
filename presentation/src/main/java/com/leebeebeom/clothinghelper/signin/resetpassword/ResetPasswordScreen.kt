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