package com.leebeebeom.clothinghelper.signin.resetpassword

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.signin.base.BaseSignInViewModelState
import com.leebeebeom.clothinghelper.signin.base.FirebaseErrorCode
import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.usecase.user.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    val viewModelState = ResetPasswordViewModelState()

    fun sendResetPasswordEmail(email: String) =
        viewModelScope.launch {
            viewModelState.loadingOn()
            resetPasswordUseCase(email, resetPasswordListener)
            viewModelState.loadingOff()
        }

    private val resetPasswordListener = object : FireBaseListeners.ResetPasswordListener {
        override fun taskSuccess() {
            viewModelState.showToast(R.string.email_send_complete)
            viewModelState.goBack()
        }

        override fun taskFailed(exception: Exception?) {
            val firebaseAuthException = exception as? FirebaseAuthException

            if (firebaseAuthException == null) {
                viewModelState.showToast(R.string.unknown_error)
                Log.d(TAG, "taskFailed: firebaseAuthException = null")
            } else setError(firebaseAuthException.errorCode)
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

class ResetPasswordViewModelState : BaseSignInViewModelState() {
    var goBack: Boolean by mutableStateOf(false)
        private set

    fun goBack() {
        goBack = true
    }

    fun wentBack() {
        goBack = false
    }
}