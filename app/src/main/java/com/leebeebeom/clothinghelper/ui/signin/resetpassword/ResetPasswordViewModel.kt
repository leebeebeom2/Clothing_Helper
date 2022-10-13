package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class ResetPasswordViewModel : SignInBaseViewModel() {
    var resetPasswordState by mutableStateOf(ResetPasswordUIState())
        private set

    val onEmailChange = { newEmail: String ->
        resetPasswordState = resetPasswordState.onEmailChange(newEmail)
    }

    fun sendResetPasswordEmail() {
        loadingOn()

        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(resetPasswordState.emailState.text)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(R.string.email_send_complete)
                    goBack()
                } else fireBaseErrorCheck(it.exception)
                loadingOff()
            }
    }

    private fun setEmailError(error: TextFieldError) {
        resetPasswordState = resetPasswordState.setEmailError(error)
    }

    private fun loadingOn() {
        resetPasswordState = resetPasswordState.loadingOn()
    }

    private fun loadingOff() {
        resetPasswordState = resetPasswordState.loadingOff()
    }

    override fun showToast(@StringRes toastText: Int) {
        resetPasswordState = resetPasswordState.showToast(toastText)
    }

    override fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> setEmailError(TextFieldError.ERROR_INVALID_EMAIL)
            FirebaseErrorCode.ERROR_USER_NOT_FOUND -> setEmailError(TextFieldError.ERROR_USER_NOT_FOUND)
            else -> showToast(R.string.unknown_error)
        }
    }

    val toastShown = {
        resetPasswordState = resetPasswordState.toastShown()
    }

    private fun goBack() {
        resetPasswordState = resetPasswordState.goBack()
    }

    fun wentBack() {
        resetPasswordState = resetPasswordState.wentBack()
    }
}

data class ResetPasswordUIState(
    val isLoading: Boolean = false,
    @StringRes val toastText: Int? = null,
    val emailState: TextFieldUIState = TextFieldUIState(
        keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
    ),
    val goBack: Boolean = false
) {
    val submitButtonEnabled get() = !emailState.isBlank && !emailState.isError

    fun onEmailChange(newEmail: String) =
        copy(emailState = emailState.textChangeAndErrorOff(newEmail))

    fun setEmailError(error: TextFieldError) = copy(emailState = emailState.errorOn(error))

    fun loadingOn() = copy(isLoading = true)

    fun loadingOff() = copy(isLoading = false)

    fun showToast(@StringRes toastText: Int) = copy(toastText = toastText)

    fun toastShown() = copy(toastText = null)

    fun goBack() = copy(goBack = true)

    fun wentBack() = copy(goBack = false)
}