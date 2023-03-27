package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.ResetPasswordUseCase
import com.leebeebeom.clothinghelper.ui.signin.ui.EmailState
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val firebaseAuthErrorUseCase: FirebaseAuthErrorUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val resetPasswordState = ResetPasswordState(savedStateHandle = savedStateHandle)
    val uiState = resetPasswordState.uiStateFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(50000),
        initialValue = ResetPasswordUiState()
    )

    fun sendResetPasswordEmail(showToast: ShowToast, setLoading: (Boolean) -> Unit) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            firebaseAuthErrorUseCase.firebaseAuthError(
                throwable = throwable,
                setEmailError = resetPasswordState.emailError::set,
                showToast = showToast
            )
            setLoading(false)
        }
        viewModelScope.launch(handler) {
            setLoading(true)
            resetPasswordUseCase.sendResetPasswordEmail(email = resetPasswordState.email.state)
            showToast(R.string.email_send_complete)
            resetPasswordState.taskSuccess()
            setLoading(false)
        }
    }
}

data class ResetPasswordUiState(
    val emailError: Int? = null,
    val buttonEnabled: Boolean = false,
    val isTaskSuccess: Boolean = false,
    val isLoading: Boolean = false
)

private const val ResetPasswordEmailKey = "reset password email"
private const val ResetPasswordEmailErrorKey = "reset password email error"

class ResetPasswordState(savedStateHandle: SavedStateHandle) : EmailState(
    savedStateHandle = savedStateHandle,
    emailKey = ResetPasswordEmailKey,
    emailErrorKey = ResetPasswordEmailErrorKey
) {
    private var isTaskSuccessState by mutableStateOf(false)
    private val isTaskSuccessFlow = snapshotFlow { isTaskSuccessState }

    fun taskSuccess() {
        isTaskSuccessState = true
    }

    fun consumeTaskSuccess() {
        isTaskSuccessState = false
    }

    val uiStateFlow = combine(
        flow = emailError.flow,
        flow2 = buttonEnabledFlow,
        flow3 = isTaskSuccessFlow,
        flow4 = isLoadingFlow
    ) { emailError, buttonEnabled, isTaskSuccess, isLoading ->
        ResetPasswordUiState(
            emailError = emailError,
            buttonEnabled = buttonEnabled,
            isTaskSuccess = isTaskSuccess,
            isLoading = isLoading
        )
    }.distinctUntilChanged()
}