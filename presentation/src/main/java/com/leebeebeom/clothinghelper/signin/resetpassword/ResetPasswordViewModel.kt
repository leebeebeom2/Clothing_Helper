package com.leebeebeom.clothinghelper.signin.resetpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.ToastUIState
import com.leebeebeom.clothinghelper.base.ToastUIStateImpl
import com.leebeebeom.clothinghelper.signin.base.interfaces.EmailUIState
import com.leebeebeom.clothinghelper.signin.base.interfaces.EmailUIStateImpl
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.user.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase) :
    ViewModel() {

    val uiState = ResetPasswordUIState()

    fun sendResetPasswordEmail() =
        viewModelScope.launch {
            when (val result = resetPasswordUseCase.sendResetPasswordEmail(uiState.email)) {
                is AuthResult.Success -> {
                    uiState.showToast(R.string.email_send_complete)
                    uiState.updateIsTaskSuccess(true)
                }
                is AuthResult.Fail -> setFireBaseError(
                    errorCode = result.errorCode,
                    updateEmailError = uiState::updateEmailError,
                    updatePasswordError = {},
                    showToast = uiState::showToast
                )
                is AuthResult.UnknownFail -> uiState.showToast(R.string.unknown_error)
            }
        }
}

class ResetPasswordUIState :
    ToastUIState by ToastUIStateImpl(),
    EmailUIState by EmailUIStateImpl() {
    var isTaskSuccess by mutableStateOf(false)
        private set

    fun updateIsTaskSuccess(taskSuccess: Boolean) {
        isTaskSuccess = taskSuccess
    }

    fun consumeIsTaskSuccess() {
        isTaskSuccess = false
    }
}