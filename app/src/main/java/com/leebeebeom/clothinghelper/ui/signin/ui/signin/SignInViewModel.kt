package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignInUseCase
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInViewModel
import com.leebeebeom.clothinghelper.ui.signin.ui.PasswordState
import com.leebeebeom.clothinghelper.ui.util.firebaseAuthErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
    savedStateHandle: SavedStateHandle
) : GoogleSignInViewModel(googleSignInUseCase = googleSignInUseCase) {

    val signInState = SignInState(savedStateHandle)
    val uiState = combine(
        flow = signInState.emailError.flow,
        flow2 = signInState.passwordError.flow,
        flow3 = signInState.buttonEnabledFlow,
        flow4 = signInState.isLoadingFlow,
        flow5 = toastTextsFlow
    ) { emailError, passwordError, buttonEnabled, isLoading, toastTexts ->
        SignInUiState(
            emailError = emailError,
            passwordError = passwordError,
            buttonEnable = buttonEnabled,
            isLoading = isLoading,
            toastTexts = toastTexts
        )
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignInUiState()
    )

    fun signInWithEmailAndPassword() {
        viewModelScope.launch(
            firebaseAuthErrorHandler(
                setEmailError = signInState.emailError::set,
                setPasswordError = signInState.passwordError::set,
                showToast = ::addToastTextAtLast,
                setLoading = signInState::setLoading
            )
        ) {
            signInState.setLoading(true)
            signInUseCase.signIn(
                email = signInState.email.state,
                password = signInState.password.state
            )
        }
    }

    override fun setLoading(loading: Boolean) {
        signInState.setLoading(loading)
    }
}

data class SignInUiState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val buttonEnable: Boolean = false,
    val isLoading: Boolean = false,
    val toastTexts: ImmutableList<Int> = emptyList<Int>().toImmutableList()
)

private const val SignInEmailKey = "sign in email"
private const val SignInEmailErrorKey = "sign in email error"
private const val SignInPasswordKey = "sign in password"
private const val SignInPasswordErrorKey = "sign in password error"

class SignInState(savedStateHandle: SavedStateHandle) : PasswordState(
    savedStateHandle = savedStateHandle,
    emailKey = SignInEmailKey,
    emailErrorKey = SignInEmailErrorKey,
    passwordKey = SignInPasswordKey,
    passwordErrorKey = SignInPasswordErrorKey
)