package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.component.HeightSpacer
import com.leebeebeom.clothinghelper.ui.component.MaxWidthButton
import com.leebeebeom.clothinghelper.ui.component.MaxWidthTextFieldWithErrorAndCancelIcon
import com.leebeebeom.clothinghelper.ui.component.rememberMaxWidthTestFieldState
import com.leebeebeom.clothinghelper.ui.main.component.ToastWrapper
import com.leebeebeom.clothinghelper.ui.signin.component.GoogleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.component.Logo
import com.leebeebeom.clothinghelper.ui.signin.component.OrDivider
import com.leebeebeom.clothinghelper.ui.signin.component.SignInBaseColumn
import com.leebeebeom.clothinghelper.ui.signin.component.textfield.EmailTextField
import com.leebeebeom.clothinghelper.ui.signin.component.textfield.PasswordTextField
import com.leebeebeom.clothinghelper.ui.signin.state.PasswordState

const val SignUpScreenTag = "sign up screen"

@Composable
fun SignUpScreen() {
    val viewModel = hiltViewModel<SignUpViewModel>()
    val state = rememberSignUpState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignInBaseColumn(modifier = Modifier.testTag(SignUpScreenTag)) {
        Logo()
        EmailTextField(
            error = { state.emailError }, onEmailChange = state::onEmailChange
        )

        val nickNameTextFieldState = rememberMaxWidthTestFieldState(blockBlank = false)

        MaxWidthTextFieldWithErrorAndCancelIcon(
            label = R.string.nickname,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            onInputChange = state::onNameChange,
            state = nickNameTextFieldState,
            onValueChange = nickNameTextFieldState::onValueChange,
            onFocusChanged = nickNameTextFieldState::onFocusChanged
        )

        PasswordTextField(
            error = { state.passwordError },
            imeAction = ImeAction.Next,
            onInputChange = state::onPasswordChange
        )

        PasswordTextField(
            label = R.string.password_confirm,
            error = { state.passwordConfirmError },
            imeAction = ImeAction.Done,
            onInputChange = state::onPasswordConfirmChange
        )
        HeightSpacer(dp = 12)

        val signUpButtonClick = remember {
            {
                viewModel.signUpWithEmailAndPassword(
                    email = state.email,
                    password = state.password,
                    name = state.name,
                    setEmailError = state::setEmailError
                )
            }
        }
        MaxWidthButton(
            text = R.string.sign_up,
            enabled = { state.buttonEnabled },
            onClick = signUpButtonClick,
        )
        OrDivider()
        GoogleSignInButton(onResult = viewModel::signInWithGoogleEmail)
    }
    CenterDotProgressIndicator(show = { uiState.isLoading })
    ToastWrapper(toastTexts = { uiState.toastTexts }, toastShown = viewModel::removeFirstToastText)
}

class SignUpScreenState(
    initialEmail: String = "",
    initialEmailError: Int? = null,
    initialName: String = "",
    initialPassword: String = "",
    initialPasswordError: Int? = null,
    initialPasswordConfirm: String = "",
    initialPasswordConfirmError: Int? = null,
) : PasswordState(
    initialEmail = initialEmail,
    initialEmailError = initialEmailError,
    initialPassword = initialPassword,
    initialPasswordError = initialPasswordError
) {
    var name by mutableStateOf(initialName)
        private set
    var passwordConfirm by mutableStateOf(initialPasswordConfirm)
        private set
    var passwordConfirmError: Int? by mutableStateOf(initialPasswordConfirmError)
        private set
    override val buttonEnabled by derivedStateOf {
        super.buttonEnabled && name.isNotBlank() && passwordConfirm.isNotBlank() && passwordConfirmError == null
    }

    fun onNameChange(name: String) {
        this.name = name
    }

    override fun onPasswordChange(password: String) {
        super.onPasswordChange(password)

        if (password.isBlank()) {
            setPasswordNotSameError()
            return
        }
        if (password.length < 6) passwordError = R.string.error_weak_password

        setPasswordNotSameError()
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        if (this.passwordConfirm == passwordConfirm) return
        this.passwordConfirm = passwordConfirm
        setPasswordNotSameError()
    }

    private fun setPasswordNotSameError() {
        if (passwordConfirm.isBlank()) {
            passwordConfirmError = null
            return
        }
        passwordConfirmError =
            if (password != passwordConfirm) R.string.error_password_confirm_not_same else null
    }

    companion object {
        val Saver = listSaver<SignUpScreenState, Any?>(
            save = {
                listOf(
                    it.email,
                    it.emailError,
                    it.name,
                    it.password,
                    it.passwordError,
                    it.passwordConfirm,
                    it.passwordConfirmError,
                )
            },
            restore = {
                SignUpScreenState(
                    initialEmail = it[0] as String,
                    initialEmailError = it[1] as Int?,
                    initialName = it[2] as String,
                    initialPassword = it[3] as String,
                    initialPasswordError = it[4] as Int?,
                    initialPasswordConfirm = it[5] as String,
                    initialPasswordConfirmError = it[6] as Int?,
                )
            }
        )
    }
}

@Composable
fun rememberSignUpState() =
    rememberSaveable(saver = SignUpScreenState.Saver) { SignUpScreenState() }