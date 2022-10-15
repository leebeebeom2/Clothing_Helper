package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class ResetPasswordViewModel : SignInBaseViewModel() {
    var state by mutableStateOf(ResetPasswordUIState())
        private set

    fun sendResetPasswordEmail() {
        loadingOn()

        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(state.emailState.text)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(R.string.email_send_complete)
                    goBack()
                } else fireBaseErrorCheck(it.exception)
                loadingOff()
            }
    }

    private fun loadingOn() {
        state = state.loadingOn()
    }

    private fun loadingOff() {
        state = state.loadingOff()
    }

    override fun showToast(@StringRes toastText: Int) {
        state = state.showToast(toastText)
    }

    override fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> state.emailState.error =
                TextFieldError.ERROR_INVALID_EMAIL
            FirebaseErrorCode.ERROR_USER_NOT_FOUND -> state.emailState.error =
                TextFieldError.ERROR_USER_NOT_FOUND
            else -> showToast(R.string.unknown_error)
        }
    }

    val toastShown = {
        state = state.toastShown()
    }

    private fun goBack() {
        state = state.goBack()
    }

    fun wentBack() {
        state = state.wentBack()
    }
}

data class ResetPasswordUIState(
    val isLoading: Boolean = false,
    @StringRes val toastText: Int? = null,
    val emailState: TextFieldUIState = TextFieldUIState.emailState(),
    val goBack: Boolean = false
) {
    val submitButtonEnabled get() = !emailState.isBlank && !emailState.isError

    fun loadingOn() = copy(isLoading = true)

    fun loadingOff() = copy(isLoading = false)

    fun showToast(@StringRes toastText: Int) = copy(toastText = toastText)

    fun toastShown() = copy(toastText = null)

    fun goBack() = copy(goBack = true)

    fun wentBack() = copy(goBack = false)
}