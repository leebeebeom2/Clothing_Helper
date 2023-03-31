package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignInUseCase
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInViewModel
import com.leebeebeom.clothinghelper.ui.state.LoadingState2
import com.leebeebeom.clothinghelper.ui.state.ToastState
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

private const val SignInLoadingKey = "sign in loading"
private const val SignInToastTextsKey = "sign in toast texts"

@HiltViewModel
class SignInViewModel @Inject constructor(
    googleSignInUseCase: GoogleSignInUseCase,
    private val signInUseCase: SignInUseCase,
    savedStateHandle: SavedStateHandle
) : GoogleSignInViewModel(
    googleSignInUseCase = googleSignInUseCase,
    savedToastTextsKey = SignInToastTextsKey,
    savedLoadingKey = SignInLoadingKey,
    savedStateHandle = savedStateHandle
) {

    val uiState = combine(
        flow = isLoadingFlow,
        flow2 = toastTextsFlow
    ) { isLoading, toastTexts ->
        SignInUiState(
            isLoading = isLoading,
            toastTexts = toastTexts
        )
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignInUiState()
    )

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        setEmailError: (Int) -> Unit,
        setPasswordError: (Int) -> Unit,
    ) {
        viewModelScope.launch(
            firebaseAuthErrorHandler(
                setEmailError = setEmailError,
                setPasswordError = setPasswordError,
                showToast = ::addToastTextAtLast,
                loadingOff = { setLoading(false) }
            )
        ) {
            setLoading(true)
            signInUseCase.signIn(email = email, password = password)
        }
    }
}

data class SignInUiState(
    override val isLoading: Boolean = false,
    override val toastTexts: ImmutableList<Int> = persistentListOf(),
) : ToastState, LoadingState2