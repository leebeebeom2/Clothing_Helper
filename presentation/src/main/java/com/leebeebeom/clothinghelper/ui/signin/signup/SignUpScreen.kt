package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.base.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.base.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.base.PasswordUIState
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseRoot

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = hiltViewModel()) {
    val state = rememberSignUpScreenUIState()
    val viewModelState = viewModel.viewModelState

    SignInBaseRoot(
        isLoading = viewModelState.isLoading,
        toastText = viewModelState.toastText,
        toastShown = viewModelState.toastShown
    ) {
        EmailTextField(
            email = state.email,
            onEmailChange = { state.onEmailChange(email = it) { viewModelState.emailErrorOff() } },
            error = viewModelState.emailError,
            imeAction = ImeAction.Next,
            showKeyboardEnable = true
        )

        MaxWidthTextField(
            label = R.string.name,
            text = state.name,
            onValueChange = state::onNameChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        PasswordTextField(
            password = state.password,
            onPasswordChange = {
                state.onPasswordChange(
                    password = it,
                    passwordErrorOff = viewModelState::passwordErrorOff,
                    passwordErrorOn = viewModelState::passwordErrorOn,
                    passwordConfirmErrorOff = viewModelState::passwordConfirmErrorOff,
                    passwordConfirmErrorOn = viewModelState::passwordConfirmErrorOn
                )
            },
            error = viewModelState.passwordError,
            imeAction = ImeAction.Next
        )

        PasswordTextField(
            password = state.passwordConfirm,
            onPasswordChange = {
                state.onPasswordConfirmChange(
                    passwordConfirm = it,
                    passwordConfirmErrorOff = viewModelState::passwordConfirmErrorOff,
                    passwordConfirmErrorOn = viewModelState::passwordConfirmErrorOn
                )
            },
            error = viewModelState.passwordConfirmError,
            imeAction = ImeAction.Done,
            label = R.string.password_confirm
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(text = R.string.sign_up,
            enabled = state.submitButtonEnabled(
                emailError = viewModelState.emailError,
                passwordError = viewModelState.passwordError,
                passwordConfirmError = viewModelState.passwordConfirmError
            ),
            onClick = {
                viewModel.signUpWithEmailAndPassword(
                    state.email,
                    state.password,
                    state.name
                )
            })
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        GoogleSignInButton(
            signInWithGoogleEmail = viewModel::signInWithGoogleEmail,
            enabled = viewModelState.googleButtonEnabled
        )
    }
}

class SignUpScreenUIState(
    initialEmail: String = "",
    initialName: String = "",
    initialPassword: String = "",
    initialPasswordConfirm: String = ""
) : PasswordUIState(initialEmail, initialPassword) {

    var name: String by mutableStateOf(initialName)
        private set
    var passwordConfirm: String by mutableStateOf(initialPasswordConfirm)
        private set

    fun onNameChange(name: String) {
        this.name = name
    }

    fun onPasswordChange(
        password: String,
        passwordErrorOff: () -> Unit,
        passwordErrorOn: (Int) -> Unit,
        passwordConfirmErrorOff: () -> Unit,
        passwordConfirmErrorOn: (Int) -> Unit
    ) {
        super.onPasswordChange(password, passwordErrorOff)
        if (password.isNotBlank()) {
            passwordSameCheck(passwordConfirmErrorOn, passwordConfirmErrorOff)
            if (password.length < 6) passwordErrorOn(R.string.error_weak_password)
        }
    }

    fun onPasswordConfirmChange(
        passwordConfirm: String,
        passwordConfirmErrorOff: () -> Unit,
        passwordConfirmErrorOn: (Int) -> Unit
    ) {
        this.passwordConfirm = passwordConfirm
        passwordConfirmErrorOff()
        passwordSameCheck(passwordConfirmErrorOn, passwordConfirmErrorOff)
    }

    private fun passwordSameCheck(
        passwordConfirmErrorOn: (Int) -> Unit, passwordConfirmErrorOff: () -> Unit
    ) {
        if (passwordConfirm.isNotBlank()) {
            if (password != passwordConfirm) passwordConfirmErrorOn(R.string.error_password_confirm_not_same)
            else passwordConfirmErrorOff()
        }
    }

    fun submitButtonEnabled(
        @StringRes emailError: Int?,
        @StringRes passwordError: Int?,
        @StringRes passwordConfirmError: Int?
    ) = email.isNotBlank() && emailError == null &&
            name.isNotBlank() &&
            password.isNotBlank() && passwordError == null &&
            passwordConfirm.isNotBlank() && passwordConfirmError == null

    companion object {
        val Saver: Saver<SignUpScreenUIState, *> = listSaver(save = {
            listOf(
                it.email,
                it.name,
                it.password,
                it.passwordConfirm
            )
        }, restore = {
            SignUpScreenUIState(it[0], it[1], it[2], it[3])
        })
    }
}

@Composable
fun rememberSignUpScreenUIState() = rememberSaveable(saver = SignUpScreenUIState.Saver) {
    SignUpScreenUIState()
}