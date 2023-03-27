package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignUpUseCase
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInState
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInViewModel
import com.leebeebeom.clothinghelper.ui.signin.ui.SavedStateProvider
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
class SignUpViewModel @Inject constructor(
    googleSignInUseCase: GoogleSignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val firebaseAuthErrorUseCase: FirebaseAuthErrorUseCase,
    savedStateHandle: SavedStateHandle
) : GoogleSignInViewModel(
    googleSignInUseCase = googleSignInUseCase, firebaseAuthErrorUseCase = firebaseAuthErrorUseCase
) {

    val signUpState = SignUpState(savedStateHandle)

    @Suppress("UNCHECKED_CAST")
    val uiState = combine(
        signUpState.emailError.flow,
        signUpState.passwordError.flow,
        signUpState.passwordConfirmError.flow,
        signUpState.buttonEnabledFlow,
        signUpState.googleButtonEnabledFlow,
        signUpState.isLoadingFlow,
        toastTextsFlow
    ) { flows ->
        SignUpUiState(
            emailError = flows[0] as Int?,
            passwordError = flows[1] as Int?,
            passwordConfirmError = flows[2] as Int?,
            buttonEnabled = flows[3] as Boolean,
            googleButtonEnabled = flows[4] as Boolean,
            isLoading = flows[5] as Boolean,
            toastTexts = (flows[6] as SnapshotStateList<Int>).toImmutableList()
        )
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignUpUiState()
    )
    override val googleSignInState = signUpState

    fun signUpWithEmailAndPassword() {
        val handler = CoroutineExceptionHandler { _, throwable ->
            firebaseAuthErrorUseCase.firebaseAuthError(
                throwable = throwable,
                setEmailError = signUpState.emailError::set,
                showToast = ::addToastTextAtLast
            )
            signUpState.setLoading(false)
        }

        viewModelScope.launch(handler) {
            signUpState.setLoading(true)
            signUpUseCase.signUp(
                email = signUpState.email.state,
                password = signUpState.password.state,
                name = signUpState.name.state.trim()
            )
        }
    }
}

data class SignUpUiState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val passwordConfirmError: Int? = null,
    val buttonEnabled: Boolean = false,
    override val googleButtonEnabled: Boolean = true,
    override val isLoading: Boolean = false,
    val toastTexts: ImmutableList<Int> = emptyList<Int>().toImmutableList()
) : GoogleSignInUiState()

private const val SignUpEmailKey = "sign up email"
private const val SignUpEmailErrorKey = "sign up email error"
private const val SignUpNameKey = "sign up name"
private const val SignUpPasswordKey = "sign up password"
private const val SignUpPasswordErrorKey = "sign up password error"
private const val SignUpPasswordConfirmKey = "sign up password confirm"
private const val SignUpPasswordConfirmErrorKey = "sign up password confirm error"

class SignUpState(savedStateHandle: SavedStateHandle) : GoogleSignInState(
    savedStateHandle = savedStateHandle,
    savedEmailKey = SignUpEmailKey,
    emailErrorKey = SignUpEmailErrorKey,
    savedPasswordKey = SignUpPasswordKey,
    passwordErrorKey = SignUpPasswordErrorKey
) {
    val name = SavedStateProvider(
        savedStateHandle = savedStateHandle, key = SignUpNameKey, initialValue = ""
    )
    val passwordConfirm = SavedStateProvider(
        savedStateHandle = savedStateHandle, key = SignUpPasswordConfirmKey, initialValue = ""
    )
    val passwordConfirmError = SavedStateProvider<Int?>(
        savedStateHandle = savedStateHandle,
        key = SignUpPasswordConfirmErrorKey,
        initialValue = null
    )
    override val buttonEnabledState by derivedStateOf {
        super.buttonEnabledState && name.state.isNotBlank() && passwordConfirm.state.isNotBlank() && passwordConfirmError.state == null
    }

    fun setName(name: String) = this.name.set(name)

    override fun setPassword(password: String) {
        super.setPassword(password)

        if (password.isBlank()) {
            setPasswordNotSameError()
            return
        }
        if (password.length < 6) passwordError.set(R.string.error_weak_password)

        setPasswordNotSameError()
    }

    fun setPasswordConfirm(passwordConfirm: String) {
        if (this.passwordConfirm.state == passwordConfirm) return
        this.passwordConfirm.set(passwordConfirm)
        setPasswordNotSameError()
    }

    private fun setPasswordNotSameError() {
        if (passwordConfirm.state.isBlank()) {
            passwordConfirmError.set(null)
            return
        }
        if (password.state != passwordConfirm.state) passwordConfirmError.set(R.string.error_password_confirm_not_same)
        else passwordConfirmError.set(null)
    }
}