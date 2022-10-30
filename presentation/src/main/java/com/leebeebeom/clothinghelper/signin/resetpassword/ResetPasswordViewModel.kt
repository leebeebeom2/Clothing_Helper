package com.leebeebeom.clothinghelper.signin.resetpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.BaseViewModelState
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    val viewModelState = ResetPasswordViewModelState()

    fun sendResetPasswordEmail() {
        resetPasswordUseCase(viewModelState.email) {
            when (it) {
                is FirebaseResult.Success -> {
                    viewModelState.showToast(R.string.email_send_complete)
                    viewModelState.updateTaskSuccess(true)
                }
                is FirebaseResult.Fail -> {
                    setFireBaseError(
                        exception = it.exception,
                        updateEmailError = viewModelState.emailState::updateError,
                        updatePasswordError = {},
                        showToast = viewModelState::showToast
                    )
                }
            }
        }
    }
}

class ResetPasswordViewModelState : BaseViewModelState() {
    val emailState = MaxWidthTextFieldState.email(imeAction = ImeAction.Done)
    val email get() = emailState.textFiled.text

    val submitButtonEnabled get() = email.isNotEmpty() && !emailState.isError

    var isTaskSuccess by mutableStateOf(false)
        private set

    fun updateTaskSuccess(isSuccess: Boolean) {
        isTaskSuccess = isSuccess
    }
}