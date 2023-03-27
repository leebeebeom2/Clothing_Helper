package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignInUseCase
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInState
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInViewModel
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

    @Suppress("UNCHECKED_CAST")
    val uiState = combine(
        signInState.emailError.flow,
        signInState.passwordError.flow,
        signInState.buttonEnabledFlow,
        signInState.googleButtonEnabledFlow,
        signInState.isLoadingFlow,
        toastTextsFlow
    ) { flows ->
        SignInUiState(
            emailError = flows[0] as Int?,
            passwordError = flows[1] as Int?,
            buttonEnable = flows[2] as Boolean,
            googleButtonEnabled = flows[3] as Boolean,
            isLoading = flows[4] as Boolean,
            toastTexts = (flows[5] as SnapshotStateList<Int>).toImmutableList()
        )
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignInUiState()
    )
    override val googleSignInState = signInState

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
}

data class SignInUiState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val buttonEnable: Boolean = false,
    override val googleButtonEnabled: Boolean = true,
    override val isLoading: Boolean = false,
    val toastTexts: ImmutableList<Int> = emptyList<Int>().toImmutableList()
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
)