package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignInUseCase
import com.leebeebeom.clothinghelper.ui.signin.base.EmailAndPasswordViewModel
import com.leebeebeom.clothinghelper.ui.signin.state.EmailAndPasswordState
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailAndPasswordUiState
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val firebaseAuthErrorUseCase: FirebaseAuthErrorUseCase,
) :
    EmailAndPasswordViewModel() {

    override val mutableUiState: MutableEmailAndPasswordUiState = MutableEmailAndPasswordUiState()
    val uiState: EmailAndPasswordState = mutableUiState

    fun signInWithEmailAndPassword(showToast: ShowToast) =
        viewModelScope.launch {
            signInUseCase.signIn(
                email = mutableUiState.email,
                password = mutableUiState.password,
                firebaseResult = object : FirebaseResult {
                    override fun success() = showToast(R.string.sign_in_complete)
                    override fun fail(exception: Exception) =
                        firebaseAuthErrorUseCase.firebaseAuthError(
                            exception = exception,
                            updateEmailError = { mutableUiState.emailError = it },
                            updatePasswordError = { mutableUiState.passwordError = it },
                            showToast = showToast
                        )
                }
            )
        }
}