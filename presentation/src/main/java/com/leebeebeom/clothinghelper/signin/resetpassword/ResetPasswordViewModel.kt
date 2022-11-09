package com.leebeebeom.clothinghelper.signin.resetpassword

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.BaseEmailUIStates
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase) :
    ViewModel() {

    val uiStates = ResetPasswordUIState()

    fun sendResetPasswordEmail() = viewModelScope.launch {
        when (val result = resetPasswordUseCase(uiStates.email)) {
            is FirebaseResult.Success -> {
                uiStates.showToast(R.string.email_send_complete)
                uiStates.updateIsTaskSuccess(true)
            }
            is FirebaseResult.Fail -> {
                setFireBaseError(
                    exception = result.exception,
                    updateEmailError = uiStates::updateEmailError,
                    updatePasswordError = {},
                    showToast = uiStates::showToast
                )
            }
        }
    }
}

class ResetPasswordUIState : BaseEmailUIStates() {
    val _isTaskSuccess = mutableStateOf(false)
    val isTaskSuccess by derivedStateOf { _isTaskSuccess.value }

    fun updateIsTaskSuccess(taskSuccess: Boolean) {
        _isTaskSuccess.value = taskSuccess
    }
}