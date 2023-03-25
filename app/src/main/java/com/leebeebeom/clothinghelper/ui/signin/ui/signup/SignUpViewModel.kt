package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignUpUseCase
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInState
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.GoogleSignInViewModel
import com.leebeebeom.clothinghelper.ui.signin.ui.SavedStateHandleDelegator
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
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
    val uiState = signUpState.uiStateStream.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignUpUiState()
    )
    override val googleSignInState = signUpState

    fun signUpWithEmailAndPassword(showToast: ShowToast, setLoading: (Boolean) -> Unit): Job {
        val handler = CoroutineExceptionHandler { _, throwable ->
            firebaseAuthErrorUseCase.firebaseAuthError(
                throwable = throwable,
                setEmailError = signUpState::setEmailError,
                showToast = showToast
            )
            setLoading(false)
        }

        return viewModelScope.launch(handler) {
            setLoading(true)
            signUpUseCase.signUp(
                email = signUpState.savedEmail,
                password = signUpState.savedPassword,
                name = signUpState.nameState
            )
        }
    }
}

data class SignUpUiState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val passwordConfirmError: Int? = null,
    val buttonEnabled: Boolean = true,
    override val googleButtonEnabled: Boolean = true,
    override val isLoading: Boolean = false
) : GoogleSignInUiState()

private const val SignUpEmailKey = "sign up email"
private const val SignUpEmailErrorKey = "sign up email error"
private const val SignUpNameKey = "sign up name"
private const val SignUpPasswordKey = "sign up password"
private const val SignUpPasswordErrorKey = "sign up password error"
private const val SignUpPasswordConfirmKey = "sign up password confirm"
private const val SignUpPasswordConfirmErrorKey = "sign up password confirm error"

class SignUpState(private val savedStateHandle: SavedStateHandle) : GoogleSignInState(
    savedStateHandle = savedStateHandle,
    savedEmailKey = SignUpEmailKey,
    emailErrorKey = SignUpEmailErrorKey,
    savedPasswordKey = SignUpPasswordKey,
    passwordErrorKey = SignUpPasswordErrorKey
) {

    var savedName by SavedStateHandleDelegator(
        savedStateHandle = savedStateHandle, key = SignUpNameKey, initial = ""
    )
        private set
    var savedPasswordConfirm by SavedStateHandleDelegator(
        savedStateHandle = savedStateHandle, key = SignUpPasswordConfirmKey, initial = ""
    )
        private set

    var nameState by mutableStateOf(savedName)
        private set
    private var passwordConfirmState by mutableStateOf(savedPasswordConfirm)

    private var passwordConfirmErrorState by mutableStateOf(
        savedStateHandle.get<Int?>(SignUpPasswordConfirmErrorKey)
    )
    override val buttonEnabledState by derivedStateOf {
        super.buttonEnabledState && nameState.isNotBlank() && passwordConfirmState.isNotBlank() && passwordConfirmErrorState == null
    }

    fun setName(name: String) {
        if (name == nameState) return
        savedName = name
        nameState = name
    }

    override fun setPassword(password: String) {
        super.setPassword(password)

        if (password.isBlank()) return
        if (password.length < 6) setPasswordError(R.string.error_weak_password)

        setPasswordNotSameError()
    }

    fun setPasswordConfirm(passwordConfirm: String) {
        if (passwordConfirm == passwordConfirmState) return
        savedPasswordConfirm = passwordConfirm
        passwordConfirmState = passwordConfirm

        setPasswordNotSameError()
    }

    private fun setPasswordNotSameError() {
        if (passwordConfirmState.isBlank()) return
        if (passwordState != passwordConfirmState) setPasswordConfirmError(R.string.error_password_confirm_not_same)
        else setPasswordConfirmError(null)
    }

    private fun setPasswordConfirmError(@StringRes passwordConfirmError: Int?) {
        if (passwordConfirmErrorState == passwordConfirmError) return
        savedStateHandle[SignUpPasswordConfirmErrorKey] = passwordConfirmError
        passwordConfirmErrorState = passwordConfirmError
    }

    private val passwordConfirmErrorStream = snapshotFlow { passwordConfirmErrorState }
    val uiStateStream = combine(
        emailErrorStream,
        passwordErrorStream,
        passwordConfirmErrorStream,
        buttonEnabledStream,
        googleButtonEnabledStream,
        isSignInLoadingStream
    ) { flows ->
        SignUpUiState(
            emailError = flows[0] as Int?,
            passwordError = flows[1] as Int?,
            passwordConfirmError = flows[2] as Int?,
            buttonEnabled = flows[3] as Boolean,
            googleButtonEnabled = flows[4] as Boolean,
            isLoading = flows[5] as Boolean
        )
    }.distinctUntilChanged()
}