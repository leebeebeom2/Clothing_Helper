package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.signin.base.PasswordUIState
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.base.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.base.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseRoot

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    val state = rememberSignUpScreenUIState()
    val viewModelState = viewModel.viewModelState

    SignInBaseRoot(
        isLoading = viewModelState.isLoading,
        toastText = viewModelState.toastText,
        toastShown = viewModelState.toastShown
    ) {
        Column {
            EmailTextField(
                email = state.email,
                onEmailChange = state::onEmailChange,
                error = state.emailError,
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
                onPasswordChange = state::onPasswordChange,
                error = state.passwordError,
                imeAction = ImeAction.Next
            )

            PasswordTextField(
                password = state.passwordConfirm,
                onPasswordChange = state::onPasswordConfirmChange,
                error = state.passwordConfirmError,
                imeAction = ImeAction.Done,
                label = R.string.password_confirm
            )
            SimpleHeightSpacer(dp = 12)
            MaxWidthButton(
                text = R.string.sign_up,
                enabled = state.submitButtonEnabled,
                onClick = {
                    viewModel.signUpWithEmailAndPassword(
                        state.email,
                        state.password,
                        state.name,
                        state::emailErrorEnabled
                    )
                }
            )
            SimpleHeightSpacer(dp = 8)
            OrDivider()
            SimpleHeightSpacer(dp = 8)
            GoogleSignInButton(
                googleSignInClick = viewModel::googleSignInLauncherLaunch,
                googleSignIn = viewModel::googleSignIn,
                enabled = viewModelState.googleButtonEnabled
            )
        }
        // 프리뷰 시 주석 처리
    }
}

class SignUpScreenUIState(
    initialEmail: String = "",
    @StringRes initialEmailError: Int? = null,
    initialName: String = "",
    initialPassword: String = "",
    @StringRes initialPasswordError: Int? = null,
    initialPasswordConfirm: String = "",
    @StringRes initialPasswordConfirmError: Int? = null,
) : PasswordUIState(initialEmail, initialEmailError, initialPassword, initialPasswordError) {

    var name: String by mutableStateOf(initialName)
        private set
    var passwordConfirm: String by mutableStateOf(initialPasswordConfirm)
        private set
    var passwordConfirmError: Int? by mutableStateOf(initialPasswordConfirmError)
        private set

    fun onNameChange(name: String) {
        this.name = name
    }

    override fun onPasswordChange(password: String) {
        super.onPasswordChange(password)
        if (password.isNotBlank()) {
            passwordSameCheck()
            if (password.length < 6)
                passwordConfirmErrorEnabled(R.string.error_weak_password)
        }
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        this.passwordConfirm = passwordConfirm
        passwordConfirmError = null
        passwordSameCheck()
    }

    private fun passwordConfirmErrorEnabled(error: Int) {
        passwordConfirmError = error
    }

    private fun passwordSameCheck() {
        if (passwordConfirm.isNotBlank()) {
            if (password != passwordConfirm)
                passwordConfirmErrorEnabled(R.string.error_password_confirm_not_same)
            else passwordConfirmError = null
        }
    }

    val submitButtonEnabled
        get() = email.isNotBlank() && emailError == null &&
                name.isNotBlank() &&
                password.isNotBlank() && passwordError == null &&
                passwordConfirm.isNotBlank() && passwordConfirmError == null

    companion object {
        val Saver: Saver<SignUpScreenUIState, *> = listSaver(
            save = {
                listOf(
                    it.email,
                    it.emailError,
                    it.name,
                    it.password,
                    it.passwordError,
                    it.passwordConfirm,
                    it.passwordConfirmError
                )
            },
            restore = {
                SignUpScreenUIState(
                    it[0] as String,
                    it[1] as? Int,
                    it[2] as String,
                    it[3] as String,
                    it[4] as? Int,
                    it[5] as String,
                    it[6] as? Int,
                )
            }
        )
    }
}

@Composable
fun rememberSignUpScreenUIState() =
    rememberSaveable(saver = SignUpScreenUIState.Saver) {
        SignUpScreenUIState()
    }