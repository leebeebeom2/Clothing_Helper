package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.AuthResult.*
import com.leebeebeom.clothinghelper.domain.usecase.user.ResetPasswordUseCase
import com.leebeebeom.clothinghelper.ui.signin.base.EmailViewModel
import com.leebeebeom.clothinghelper.ui.signin.state.EmailUiState
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailUiState
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import com.leebeebeom.clothinghelper.ui.util.fireBaseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase) :
    EmailViewModel() {

    override val mutableUiState: MutableResetPasswordUiState = MutableResetPasswordUiState()
    val uiState: ResetPasswordUiState = mutableUiState

    fun consumeTaskSuccess() {
        mutableUiState.isTaskSuccess = false
    }

    fun sendResetPasswordEmail(showToast: ShowToast) {
        viewModelScope.launch {
            when (val result =
                resetPasswordUseCase.sendResetPasswordEmail(email = mutableUiState.email)) {
                is Success -> {
                    showToast(R.string.email_send_complete)
                    mutableUiState.isTaskSuccess = true
                }
                is Fail ->
                    fireBaseError(
                        errorCode = result.errorCode,
                        updateEmailError = { mutableUiState.emailError = it },
                        showToast = showToast
                    )
                is UnknownFail -> showToast(R.string.unknown_error)
            }
        }
    }
}

@Stable
interface ResetPasswordUiState : EmailUiState {
    val isTaskSuccess: Boolean
}

class MutableResetPasswordUiState : MutableEmailUiState(), ResetPasswordUiState {
    override var isTaskSuccess by mutableStateOf(false)
}