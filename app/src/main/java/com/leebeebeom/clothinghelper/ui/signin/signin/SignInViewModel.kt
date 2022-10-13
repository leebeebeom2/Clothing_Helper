package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl

class SignInViewModel : ViewModel(), GoogleSignInImpl {
    var signInState by mutableStateOf(SignInUIState())
        private set

    val onEmailChange = { newEmail: String ->
        signInState = signInState.onEmailChange(newEmail)
    }

    val onPasswordChange = { newPassword: String ->
        signInState = signInState.onPasswordChange(newPassword)
    }

    val passwordVisualTransformationToggle = { isVisible: Boolean ->
        signInState = signInState.passwordVisualTransformationToggle(isVisible)
    }

    fun signInWithEmailAndPassword() {
        loadingOn()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            signInState.emailState.text,
            signInState.passwordState.text
        ).addOnCompleteListener {
            if (it.isSuccessful) showToast(R.string.login_complete)
            else setFirebaseError(it.exception)
            loadingOff()
        }
    }

    private fun setFirebaseError(exception: Exception?) {
        val firebaseException = (exception as? FirebaseAuthException)

        if (firebaseException == null) showToast(R.string.unknown_error)
        else when (firebaseException.errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> setEmailError(TextFieldError.ERROR_INVALID_EMAIL)
            FirebaseErrorCode.ERROR_USER_NOT_FOUND -> setEmailError(TextFieldError.ERROR_USER_NOT_FOUND)
            FirebaseErrorCode.ERROR_WRONG_PASSWORD -> setWrongPasswordError()
            else -> showToast(R.string.unknown_error)
        }
    }

    private fun setWrongPasswordError() {
        signInState = signInState.setPasswordError(TextFieldError.ERROR_WRONG_PASSWORD)
    }

    private fun setEmailError(error: TextFieldError) {
        signInState = signInState.setEmailError(error)
    }

    private fun showToast(@StringRes toastText: Int) {
        signInState = signInState.showToast(toastText)
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
        signInState = signInState.setGoogleButtonenabled(enabled)
    }
}

data class SignInUIState(
    val emailState: TextFieldUIState = TextFieldUIState(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Email
    ),
    val passwordState: TextFieldUIState = TextFieldUIState(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password,
        visualTransformation = PasswordVisualTransformation()
    ),
    val isLoading: Boolean = false,
    @StringRes val toastText: Int? = null,
    val googleButtonEnabled: Boolean = true
) {
    val loginButtonEnabled
        get() = !emailState.isBlank && !emailState.isError
                && !passwordState.isBlank && !passwordState.isError

    fun onEmailChange(newEmail: String) =
        copy(emailState = emailState.textChangeAndErrorOff(newEmail))

    fun onPasswordChange(newPassword: String) =
        copy(passwordState = passwordState.textChangeAndErrorOff(newPassword))

    fun passwordVisualTransformationToggle(isVisible: Boolean) =
        if (isVisible) copy(passwordState = passwordState.setInvisible())
        else copy(passwordState = passwordState.setVisible())

    fun loadingOn() = copy(isLoading = true)

    fun loadingOff() = copy(isLoading = false)

    fun showToast(@StringRes toastText: Int) = copy(toastText = toastText)

    fun toastShown() = copy(toastText = null)

    fun setEmailError(error: TextFieldError) = copy(emailState = emailState.errorOn(error))

    fun setPasswordError(error: TextFieldError) = copy(passwordState = passwordState.errorOn(error))

    fun setGoogleButtonenabled(enabled: Boolean) = copy(googleButtonEnabled = enabled)
}