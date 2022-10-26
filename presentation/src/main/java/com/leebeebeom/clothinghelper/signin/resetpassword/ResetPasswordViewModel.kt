package com.leebeebeom.clothinghelper.signin.resetpassword

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.base.GoBackViewModelState
import com.leebeebeom.clothinghelper.signin.base.EmailViewModelState
import com.leebeebeom.clothinghelper.signin.base.FirebaseErrorCode
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.usecase.user.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    val viewModelState = ResetPasswordViewModelState()

    fun sendResetPasswordEmail(email: String) {
        viewModelState.loadingOn()
        resetPasswordUseCase(email, resetPasswordListener)
    }

    private val resetPasswordListener = object : FirebaseListener {
        override fun taskSuccess() {
            viewModelState.showToast(R.string.email_send_complete)
            viewModelState.goBack()
            viewModelState.loadingOff()
        }

        override fun taskFailed(exception: Exception?) {
            val firebaseAuthException = exception as? FirebaseAuthException

            if (firebaseAuthException == null) {
                viewModelState.showToast(R.string.unknown_error)
                Log.d(TAG, "taskFailed: firebaseAuthException = null")
            } else setError(firebaseAuthException.errorCode)
            viewModelState.loadingOff()
        }
    }

    private fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> viewModelState.showEmailError(R.string.error_invalid_email)
            FirebaseErrorCode.ERROR_USER_NOT_FOUND -> viewModelState.showEmailError(R.string.error_user_not_found)
            else -> {
                viewModelState.showToast(R.string.unknown_error)
                Log.d(TAG, "setError: $errorCode")
            }
        }
    }
}

class ResetPasswordViewModelState : EmailViewModelState(), GoBackViewModelState {
    override var goBack by mutableStateOf(false)
}