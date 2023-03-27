package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.ResetPasswordUseCase
import com.leebeebeom.clothinghelper.ui.ToastViewModel
import com.leebeebeom.clothinghelper.ui.signin.ui.EmailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
) : ToastViewModel() {

    val resetPasswordState = ResetPasswordState(savedStateHandle = savedStateHandle)
    val uiState = combine(
        flow = resetPasswordState.emailError.flow,
        flow2 = resetPasswordState.buttonEnabledFlow,
        flow3 = resetPasswordState.isLoadingFlow,
        flow4 = toastTextsFlow
    ) { emailError, buttonEnabled, isLoading, toastTexts ->
        ResetPasswordUiState(
            emailError = emailError,
            buttonEnabled = buttonEnabled,
            isLoading = isLoading,
            toastTexts = toastTexts.toImmutableList()
        )
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(50000),
        initialValue = ResetPasswordUiState()
    )

    fun sendResetPasswordEmail(popBackStack: () -> Unit) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            firebaseAuthErrorUseCase.firebaseAuthError(
                throwable = throwable,
                setEmailError = resetPasswordState.emailError::set,
                showToast = ::addToastTextAtLast
            )
            resetPasswordState.setLoading(false)
        }
        viewModelScope.launch(handler) {
            resetPasswordState.setLoading(true)
            resetPasswordUseCase.sendResetPasswordEmail(email = resetPasswordState.email.state)
            addToastTextAtLast(R.string.email_send_complete)
            popBackStack()
        }
    }
}

data class ResetPasswordUiState(
    val emailError: Int? = null,
    val buttonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val toastTexts: ImmutableList<Int> = emptyList<Int>().toImmutableList()
)

private const val ResetPasswordEmailKey = "reset password email"
private const val ResetPasswordEmailErrorKey = "reset password email error"

class ResetPasswordState(savedStateHandle: SavedStateHandle) : EmailState(
    savedStateHandle = savedStateHandle,
    emailKey = ResetPasswordEmailKey,
    emailErrorKey = ResetPasswordEmailErrorKey
)