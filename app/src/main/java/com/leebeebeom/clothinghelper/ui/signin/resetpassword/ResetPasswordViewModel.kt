package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.signin.base.BaseViewModelState
import com.leebeebeom.clothinghelper.ui.signin.base.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseViewModel

class ResetPasswordViewModel : SignInBaseViewModel() {
    val viewModelState by mutableStateOf(ResetPasswordViewModelState())

    fun sendResetPasswordEmail(email: String, emailErrorEnabled: (Int) -> Unit) {
        viewModelState.loadingOn()

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                showToast(R.string.email_send_complete)
                viewModelState.goBack()
            } else fireBaseErrorCheck(it.exception, emailErrorEnabled) {}
            viewModelState.loadingOff()
        }
    }

    override fun showToast(@StringRes toastText: Int) = viewModelState.showToast(toastText)

    override fun setError(
        errorCode: String, emailErrorEnabled: (Int) -> Unit, passwordErrorEnabled: (Int) -> Unit
    ) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> emailErrorEnabled(R.string.error_invalid_email)
            FirebaseErrorCode.ERROR_USER_NOT_FOUND -> emailErrorEnabled(R.string.error_user_not_found)
            else -> showToast(R.string.unknown_error)
        }
    }
}

class ResetPasswordViewModelState : BaseViewModelState() {
    var goBack: Boolean by mutableStateOf(false)
        private set

    fun goBack() {
        goBack = true
    }

    fun wentBack() {
        goBack = false
    }
}