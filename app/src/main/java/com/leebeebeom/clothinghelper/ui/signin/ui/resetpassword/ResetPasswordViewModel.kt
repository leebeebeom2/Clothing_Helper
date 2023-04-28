package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.ResetPasswordUseCase
import com.leebeebeom.clothinghelper.ui.state.LoadingUiState
import com.leebeebeom.clothinghelper.ui.state.ToastUiState
import com.leebeebeom.clothinghelper.ui.util.firebaseAuthErrorHandler
import com.leebeebeom.clothinghelper.ui.viewmodel.LoadingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ResetPasswordLoadingKey = "reset password loading"
private const val ResetPasswordToastTextsKey = "reset password toast texts"

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase, savedStateHandle: SavedStateHandle
) : LoadingViewModel(
    initialLoading = false,
    savedToastTextsKey = ResetPasswordToastTextsKey,
    savedLoadingKey = ResetPasswordLoadingKey,
    savedStateHandle = savedStateHandle
) {

    val uiState = combine(
        flow = isLoadingFlow, flow2 = toastTextsFlow
    ) { isLoading, toastTexts ->
        ResetPasswordUiState(
            isLoading = isLoading, toastTexts = toastTexts
        )
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(50000),
        initialValue = ResetPasswordUiState()
    )

    fun sendResetPasswordEmail(
        email: String,
        setEmailError: (Int) -> Unit,
        onEmailSendSuccess: () -> Unit
    ) {
        viewModelScope.launch(
            firebaseAuthErrorHandler(
                setEmailError = setEmailError,
                showToast = ::addToastTextAtLast,
                loadingOff = { setLoading(false) }
            )
        ) {
            setLoading(true)
            resetPasswordUseCase.sendResetPasswordEmail(email = email)
            addToastTextAtLast(R.string.email_send_complete)
            onEmailSendSuccess()
        }
    }
}

data class ResetPasswordUiState(
    override val isLoading: Boolean = false,
    override val toastTexts: ImmutableList<Int> = persistentListOf()
) : ToastUiState, LoadingUiState