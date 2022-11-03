package com.leebeebeom.clothinghelper.signin.resetpassword

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.BaseSignInViewModel
import com.leebeebeom.clothinghelper.signin.base.BaseSignInUIState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseSignInViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUIState())
    val uiState get() = _uiState.asStateFlow()

    fun sendResetPasswordEmail(email: String) {
        resetPasswordUseCase(email) {
            when (it) {
                is FirebaseResult.Success -> {
                    showToast(R.string.email_send_complete)
                    updateTaskSuccess(true)
                }
                is FirebaseResult.Fail -> {
                    setFireBaseError(
                        exception = it.exception,
                        updateEmailError = ::updateEmailError,
                        updatePasswordError = {},
                        showToast = ::showToast
                    )
                }
            }
        }
    }

    override fun showToast(toastText: Int?) = _uiState.update { it.copy(toastText = toastText) }

    override fun toastShown() = _uiState.update { it.copy(toastText = null) }

    override fun updateEmailError(error: Int?) = _uiState.update { it.copy(emailError = error) }

    fun updateTaskSuccess(isTaskSuccess: Boolean) =
        _uiState.update { it.copy(isTaskSuccess = isTaskSuccess) }
}

data class ResetPasswordUIState(
    val emailError: Int? = null,
    override val toastText: Int? = null,
    val isTaskSuccess: Boolean = false
) : BaseSignInUIState() {
    override val isNotError get() = emailError == null
}