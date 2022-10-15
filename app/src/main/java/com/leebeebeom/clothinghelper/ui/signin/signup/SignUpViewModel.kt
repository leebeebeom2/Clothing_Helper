package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.UserRepository
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignUpViewModel : SignInBaseViewModel(), GoogleSignInImpl {
    var signUpState by mutableStateOf(SignUpUIState())
        private set

    val onPasswordChange = { newPassword: String ->
        if (newPassword.isNotBlank()) {
            passwordSameCheck()
            if (newPassword.length < 6)
                signUpState.passwordState.error = TextFieldError.ERROR_WEAK_PASSWORD
        }
    }

    val onPasswordConfirmChange = { _: String ->
        passwordSameCheck()
    }

    private fun passwordSameCheck() {
        val passwordConfirmState = signUpState.passwordConfirmState
        val passwordSate = signUpState.passwordState

        if (!passwordConfirmState.isBlank && passwordSate.text != passwordConfirmState.text)
            signUpState.passwordConfirmState.error = TextFieldError.ERROR_PASSWORD_CONFIRM_NOT_SAME
    }

    fun signUpWithEmailAndPassword() {
        loadingOn()

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(
                signUpState.emailState.text,
                signUpState.passwordState.text
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(R.string.sign_up_complete)
                    if (it.result.user == null) showToast(R.string.name_update_failed)
                    else userNameUpdate(it.result.user!!)
                } else fireBaseErrorCheck(it.exception)
            }
    }

    private fun userNameUpdate(user: FirebaseUser) {
        val request = userProfileChangeRequest { displayName = signUpState.nameState.text }

        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) UserRepository.userNameUpdate(signUpState.nameState.text)
            else showToast(R.string.name_update_failed)
            loadingOff()
        }
    }


    private fun loadingOn() {
        signUpState = signUpState.loadingOn()
    }

    private fun loadingOff() {
        signUpState = signUpState.loadingOff()
    }

    override fun showToast(@StringRes toastText: Int) {
        signUpState = signUpState.showToast(toastText = toastText)
    }

    override fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL ->
                signUpState.emailState.error = TextFieldError.ERROR_INVALID_EMAIL
            FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE ->
                signUpState.emailState.error = TextFieldError.ERROR_EMAIL_ALREADY_IN_USE

            else -> showToast(R.string.unknown_error)
        }
        loadingOff()
    }

    val toastShown = {
        signUpState = signUpState.toastShown()
    }

    override fun googleSignInTaskFinished(@StringRes toastText: Int) {
        showToast(toastText)
        loadingOff()
        setGoogleButtonEnabled(true)
    }

    override fun googleSignInTaskStart() {
        loadingOn()
        setGoogleButtonEnabled(false)
    }

    private fun setGoogleButtonEnabled(enabled: Boolean) {
        signUpState = signUpState.setGoogleButtonEnabled(enabled)
    }
}

data class SignUpUIState(
    val emailState: TextFieldUIState = TextFieldUIState.emailState(imeAction = ImeAction.Next),
    val nameState: TextFieldUIState = TextFieldUIState(
        imeAction = ImeAction.Next,
        label = R.string.name
    ),
    val passwordState: TextFieldUIState = TextFieldUIState.passwordState(imeAction = ImeAction.Next),
    val passwordConfirmState: TextFieldUIState = TextFieldUIState(
        keyboardType = KeyboardType.Password,
        initialVisualTransformation = PasswordVisualTransformation(),
        label = R.string.password_confirm
    ),
    val isLoading: Boolean = false,
    @StringRes val toastText: Int? = null,
    val googleButtonEnabled: Boolean = true,
) {
    val signUpButtonEnabled
        get() = !emailState.isBlank && !emailState.isError &&
                !nameState.isBlank && !nameState.isError &&
                !passwordState.isBlank && !passwordState.isError &&
                !passwordConfirmState.isBlank && !passwordConfirmState.isError

    fun setGoogleButtonEnabled(enabled: Boolean) = copy(googleButtonEnabled = enabled)

    fun loadingOn() = copy(isLoading = true)

    fun loadingOff() = copy(isLoading = false)

    fun showToast(@StringRes toastText: Int) = copy(toastText = toastText)

    fun toastShown() = copy(toastText = null)
}