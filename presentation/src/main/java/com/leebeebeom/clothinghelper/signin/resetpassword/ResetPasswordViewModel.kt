package com.leebeebeom.clothinghelper.signin.resetpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.BaseEmailUIState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.user.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase) :
    ViewModel() {

    val uiStates = ResetPasswordUIState()

    fun sendResetPasswordEmail() {
        viewModelScope.launch {
            when (val result = resetPasswordUseCase(uiStates.email)) {
                is AuthResult.Success -> {
                    uiStates.showToast(R.string.email_send_complete)
                    uiStates.updateIsTaskSuccess(true)
                }
                is AuthResult.Fail -> {
                    setFireBaseError(
                        errorCode = result.errorCode,
                        updateEmailError = uiStates::updateEmailError,
                        updatePasswordError = {},
                        showToast = uiStates::showToast
                    )
                }
                is AuthResult.UnknownFail -> uiStates.showToast(R.string.unknown_error)
            }
        }
    }
}

class ResetPasswordUIState : BaseEmailUIState() {
    var isTaskSuccess by mutableStateOf(false)
        private set

    fun updateIsTaskSuccess(taskSuccess: Boolean) {
        isTaskSuccess = taskSuccess
    }

    fun consumeIsTaskSuccess() {
        isTaskSuccess = false
    }
}