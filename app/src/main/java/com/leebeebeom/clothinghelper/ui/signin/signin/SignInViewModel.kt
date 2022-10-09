package com.leebeebeom.clothinghelper.ui.signin.signin

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
        emailUpdate(signInState.emailState.textChangeAndErrorOff(newEmail))
    }

    val onPasswordChange = { newPassword: String ->
        passwordUpdate(signInState.passwordState.textChangeAndErrorOff(newPassword))
    }

    val passwordVisualTransformationToggle = { isVisible: Boolean ->
        passwordUpdate(
            if (isVisible) signInState.passwordState.setInvisible()
            else signInState.passwordState.setVisible()
        )
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
        when ((exception as FirebaseAuthException).errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> setEmailError(TextFieldError.ERROR_INVALID_EMAIL)
            FirebaseErrorCode.ERROR_USER_NOT_FOUND -> setEmailError(TextFieldError.ERROR_USER_NOT_FOUND)
            FirebaseErrorCode.ERROR_WRONG_PASSWORD -> setWrongPasswordError()
            else -> showToast(R.string.unknown_error)
        }
    }

    private fun setWrongPasswordError() =
        passwordUpdate(signInState.passwordState.errorOn(TextFieldError.ERROR_WRONG_PASSWORD))

    private fun setEmailError(error: TextFieldError) =
        emailUpdate(signInState.emailState.errorOn(error))

    override fun showToast(resId: Int) = update(signInState.copy(toastTextId = resId))

    val toastShown = update(signInState.copy(toastTextId = null))

    override fun loadingOff() = update(signInState.copy(isLoading = false))

    override fun loadingOn() = update(signInState.copy(isLoading = true))

    private fun emailUpdate(newEmailState: TextFieldUIState) =
        update(signInState.copy(emailState = newEmailState))

    private fun passwordUpdate(newPasswordState: TextFieldUIState) =
        update(signInState.copy(passwordState = newPasswordState))

    private fun update(newState: SignInUIState) {
        signInState = newState
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
    val toastTextId: Int? = null
) {
    val loginButtonEnable
        get() = !emailState.isBlank && !emailState.isError
                && !passwordState.isBlank && !passwordState.isError
}