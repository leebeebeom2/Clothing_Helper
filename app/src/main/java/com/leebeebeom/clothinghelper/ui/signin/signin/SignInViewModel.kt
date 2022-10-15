package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignInViewModel : SignInBaseViewModel(), GoogleSignInImpl {
    var signInState by mutableStateOf(SignInUIState())
        private set

    fun signInWithEmailAndPassword() {
        loadingOn()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            signInState.emailState.text,
            signInState.passwordState.text
        ).addOnCompleteListener {
            if (it.isSuccessful) showToast(R.string.login_complete)
            else fireBaseErrorCheck(it.exception)
            loadingOff()
        }
    }

    override fun showToast(@StringRes toastText: Int) {
        signInState = signInState.showToast(toastText)
    }

    override fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL ->
                signInState.emailState.error = TextFieldError.ERROR_INVALID_EMAIL
            FirebaseErrorCode.ERROR_USER_NOT_FOUND ->
                signInState.emailState.error = TextFieldError.ERROR_USER_NOT_FOUND
            FirebaseErrorCode.ERROR_WRONG_PASSWORD ->
                signInState.passwordState.error = TextFieldError.ERROR_WRONG_PASSWORD
            else -> showToast(R.string.unknown_error)
        }
    }

    val toastShown = { signInState = signInState.toastShown() }

    private fun loadingOff() {
        signInState = signInState.loadingOff()
    }

    private fun loadingOn() {
        signInState = signInState.loadingOn()
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
        signInState = signInState.setGoogleButtonEnabled(enabled)
    }
}

data class SignInUIState(
    val emailState: TextFieldUIState = TextFieldUIState.emailState(ImeAction.Next),
    val passwordState: TextFieldUIState = TextFieldUIState.passwordState(),
    val isLoading: Boolean = false,
    @StringRes val toastText: Int? = null,
    val googleButtonEnabled: Boolean = true
) {
    val loginButtonEnabled
        get() = !emailState.isBlank && !emailState.isError
                && !passwordState.isBlank && !passwordState.isError

    fun loadingOn() = copy(isLoading = true)

    fun loadingOff() = copy(isLoading = false)

    fun showToast(@StringRes toastText: Int) = copy(toastText = toastText)

    fun toastShown() = copy(toastText = null)

    fun setGoogleButtonEnabled(enabled: Boolean) = copy(googleButtonEnabled = enabled)
}