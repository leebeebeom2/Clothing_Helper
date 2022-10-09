package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode

class ResetPasswordViewModel : ViewModel() {

    var resetPasswordState by mutableStateOf(ResetPasswordUIState())
        private set

    val onEmailChange = { newEmail: String ->
        emailUpdate(resetPasswordState.emailState.textChangeAndErrorOff(newEmail))

    }

    fun sendResetPasswordEmail() {
        loadingOn()

        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(resetPasswordState.emailState.text)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(R.string.email_send_complete)
                    update(resetPasswordState.copy(isTaskSuccessful = true))
                } else setFirebaseError(it.exception)
                loadingOff()
            }
    }

    private fun setFirebaseError(exception: Exception?) {
        when ((exception as FirebaseAuthException).errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> setEmailError(TextFieldError.ERROR_INVALID_EMAIL)
            FirebaseErrorCode.ERROR_USER_NOT_FOUND -> setEmailError(TextFieldError.ERROR_USER_NOT_FOUND)
            else -> showToast(R.string.unknown_error)
        }
    }

    private fun setEmailError(error: TextFieldError) =
        emailUpdate(resetPasswordState.emailState.errorOn(error))

    private fun loadingOn() =
        update(resetPasswordState.copy(isLoading = true))

    private fun loadingOff() =
        update(resetPasswordState.copy(isLoading = false))

    fun showToast(resId: Int) = update(resetPasswordState.copy(toastTextId = resId))

    private fun emailUpdate(newEmailState: TextFieldUIState) =
        update(resetPasswordState.copy(emailState = newEmailState))

    val toastShown = { update(resetPasswordState.copy(toastTextId = null)) }

    private fun update(newState: ResetPasswordUIState) {
        resetPasswordState = newState
    }

    fun onBackPressed() =
        update(resetPasswordState.copy(isTaskSuccessful = false))
}

data class ResetPasswordUIState(
    val isLoading: Boolean = false,
    val toastTextId: Int? = null,
    val emailState: TextFieldUIState = TextFieldUIState(
        keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
    ),
    val isTaskSuccessful: Boolean = false
) {
    val checkButtonEnable get() = !emailState.isBlank && !emailState.isError
}