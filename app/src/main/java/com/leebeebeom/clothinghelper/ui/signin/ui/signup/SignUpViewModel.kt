package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignUpUseCase
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInViewModel
import com.leebeebeom.clothinghelper.ui.state.LoadingUiState
import com.leebeebeom.clothinghelper.ui.state.ToastUiState
import com.leebeebeom.clothinghelper.ui.util.firebaseAuthErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SignUpLoadingKey = "sign up loading"
private const val SignUpToastTextsKey = "sign up toast texts"

@HiltViewModel
class SignUpViewModel @Inject constructor(
    googleSignInUseCase: GoogleSignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    savedStateHandle: SavedStateHandle
) : GoogleSignInViewModel(
    googleSignInUseCase = googleSignInUseCase,
    savedToastTextsKey = SignUpToastTextsKey,
    savedLoadingKey = SignUpLoadingKey,
    savedStateHandle = savedStateHandle
) {

    val uiState = combine(
        flow = isLoadingFlow, flow2 = toastTextsFlow
    ) { isLoading, toastTexts ->
        SignUpUiState(
            isLoading = isLoading, toastTexts = toastTexts
        )
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignUpUiState()
    )

    fun signUpWithEmailAndPassword(
        email: String, name: String, password: String, setEmailError: (Int) -> Unit
    ) {
        viewModelScope.launch(
            firebaseAuthErrorHandler(setEmailError = setEmailError,
                showToast = ::addToastTextAtLast,
                loadingOff = { setLoading(false) })
        ) {
            setLoading(true)
            signUpUseCase.signUp(
                email = email, password = password, name = name.trim()
            )
        }
    }
}

data class SignUpUiState(
    override val isLoading: Boolean = false,
    override val toastTexts: ImmutableList<Int> = persistentListOf()
) : ToastUiState, LoadingUiState