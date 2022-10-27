package com.leebeebeom.clothinghelper.signin.resetpassword

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.signin.base.EmailViewModelState
import com.leebeebeom.clothinghelper.signin.base.FirebaseErrorCode
import com.leebeebeom.clothinghelper.signin.base.TaskSuccessViewModelState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
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
        resetPasswordUseCase(email, resetPasswordListener) { viewModelState.loadingOff }
    }

    private val resetPasswordListener = object : FirebaseListener {
        override fun taskSuccess() {
            viewModelState.showToast(R.string.email_send_complete)
            viewModelState.taskSuccess()
        }

        override fun taskFailed(exception: Exception?) =
            setFireBaseError(
                exception = exception,
                showEmailError = viewModelState.showEmailError,
                showPasswordError = {},
                showToast = viewModelState.showToast
            )
    }
}

class ResetPasswordViewModelState : EmailViewModelState(), TaskSuccessViewModelState {
    override var taskSuccess by mutableStateOf(false)
}