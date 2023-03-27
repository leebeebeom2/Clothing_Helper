package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignInUseCase
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInState
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInViewModel
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
class SignInViewModel @Inject constructor(
    googleSignInUseCase: GoogleSignInUseCase,
    private val signInUseCase: SignInUseCase,
    private val firebaseAuthErrorUseCase: FirebaseAuthErrorUseCase,
    savedStateHandle: SavedStateHandle
) : GoogleSignInViewModel(
    googleSignInUseCase = googleSignInUseCase, firebaseAuthErrorUseCase = firebaseAuthErrorUseCase
) {
    val signInState = SignInState(savedStateHandle)
    val uiState = signInState.uiStateFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignInUiState()
    )
    override val googleSignInState = signInState

    fun signInWithEmailAndPassword(showToast: ShowToast) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            firebaseAuthErrorUseCase.firebaseAuthError(
                throwable = throwable,
                setEmailError = signInState.emailError::set,
                setPasswordError = signInState.passwordError::set,
                showToast = showToast
            )
            signInState.setLoading(false)
        }
        viewModelScope.launch(handler) {
            signInState.setLoading(true)
            signInUseCase.signIn(
                email = signInState.email.state,
                password = signInState.password.state
            )
        }
    }
}

data class SignInUiState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val buttonEnable: Boolean = false,
    override val googleButtonEnabled: Boolean = true,
    override val isLoading: Boolean = false
) : GoogleSignInUiState()

private const val SignInEmailKey = "sign in email"
private const val SignInEmailErrorKey = "sign in email error"
private const val SignInPasswordKey = "sign in password"
private const val SignInPasswordErrorKey = "sign in password error"

class SignInState(savedStateHandle: SavedStateHandle) : GoogleSignInState(
    savedStateHandle = savedStateHandle,
    savedEmailKey = SignInEmailKey,
    emailErrorKey = SignInEmailErrorKey,
    savedPasswordKey = SignInPasswordKey,
    passwordErrorKey = SignInPasswordErrorKey
) {
    val uiStateFlow = combine(
        flow = emailError.flow,
        flow2 = passwordError.flow,
        flow3 = buttonEnabledFlow,
        flow4 = googleButtonEnabledFlow,
        flow5 = isSignInLoadingFlow
    ) { emailError, passwordError, buttonEnabled, googleButtonEnabled, isLoading ->
        SignInUiState(
            emailError = emailError,
            passwordError = passwordError,
            buttonEnable = buttonEnabled,
            googleButtonEnabled = googleButtonEnabled,
            isLoading = isLoading
        )
    }.distinctUntilChanged()
}