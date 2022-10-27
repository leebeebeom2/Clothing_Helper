package com.leebeebeom.clothinghelper.signin.signup

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
import com.leebeebeom.clothinghelper.base.MaxWidthButton
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.signin.base.*

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state = rememberSignUpScreenUIState()
    val viewModelState = viewModel.viewModelState

    SignInBaseRoot(
        isLoading = viewModelState.isLoading,
        toastText = viewModelState.toastText,
        toastShown = viewModelState.toastShown
    ) {
        EmailTextField(
            email = state.text,
            onEmailChange = { state.onTextChange(it, viewModelState.hideEmailError) },
            error = viewModelState.emailError,
            imeAction = ImeAction.Next
        )

        MaxWidthTextField(
            label = R.string.name,
            text = state.text2,
            onValueChange = { state.onText2Change(it) {} },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        PasswordTextField(
            password = state.text3,
            onPasswordChange = state::onPasswordChange,
            error = state.passwordError,
            imeAction = ImeAction.Next
        )

        PasswordTextField(
            password = state.text4,
            onPasswordChange = state::onPasswordConfirmChange,
            error = state.passwordConfirmError,
            imeAction = ImeAction.Done,
            label = R.string.password_confirm
        )
        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(text = R.string.sign_up,
            enabled = state.submitButtonEnabled(emailError = viewModelState.emailError),
            onClick = {
                viewModel.signUpWithEmailAndPassword(
                    email = state.text,
                    name = state.text2,
                    password = state.text3
                    )
            })
        SimpleHeightSpacer(dp = 8)
        OrDivider()
        SimpleHeightSpacer(dp = 8)
        // 프리뷰 시 주석처리
        GoogleSignInButton(
            signInWithGoogleEmail = viewModel::signInWithGoogleEmail,
            enabled = viewModelState.googleButtonEnabled,
            onGoogleSignInClick = viewModel.taskStart
        )
    }
}

class SignUpScreenUIState(
    email: String = "",
    name: String = "",
    password: String = "",
    passwordConfirm: String = "",
    @StringRes passwordError: Int? = null,
    @StringRes passwordConfirmError: Int? = null
) : FourTextFieldState(email, name, password, passwordConfirm) {

    var passwordError: Int? by mutableStateOf(passwordError)
        private set
    var passwordConfirmError: Int? by mutableStateOf(passwordConfirmError)
        private set

    private val showPasswordError = { error: Int? -> this.passwordError = error }
    private val hidePasswordError = { this.passwordError = null }
    private val showPasswordConfirmError = { error: Int? -> this.passwordConfirmError = error }
    private val hidePasswordConfirmError = { this.passwordConfirmError = null }

    fun onPasswordChange(password: String) {
        super.onText3Change(password, hidePasswordError)
        if (password.isNotBlank()) {
            passwordSameCheck()
            if (password.length < 6) showPasswordError(R.string.error_weak_password)
        }
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        super.onText4Change(passwordConfirm, hidePasswordConfirmError)
        passwordSameCheck()
    }

    private fun passwordSameCheck() {
        if (text4.isNotBlank()) {
            if (text3 != text4) showPasswordConfirmError(R.string.error_password_confirm_not_same)
            else hidePasswordConfirmError()
        }
    }

    fun submitButtonEnabled(@StringRes emailError: Int?) =
        text.isNotBlank() && emailError == null &&
                text2.isNotBlank() &&
                text3.isNotBlank() && passwordError == null &&
                text4.isNotBlank() && passwordConfirmError == null

    companion object {
        val Saver: Saver<SignUpScreenUIState, *> = listSaver(save = {
            listOf(
                it.text,
                it.text2,
                it.text3,
                it.text4,
                it.passwordError,
                it.passwordConfirmError
            )
        }, restore = {
            SignUpScreenUIState(
                it[0] as String,
                it[1] as String,
                it[2] as String,
                it[3] as String,
                it[4] as? Int,
                it[5] as? Int
            )
        })
    }
}

@Composable
fun rememberSignUpScreenUIState() = rememberSaveable(saver = SignUpScreenUIState.Saver) {
    SignUpScreenUIState()
}