package com.leebeebeom.clothinghelper.signin.signin

import androidx.annotation.StringRes
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInViewModelState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    private val _uiState = MutableStateFlow(SignInUIState())
    val uiState get() = _uiState.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        signInUseCase(email, password) {
            when (it) {
                is FirebaseResult.Success -> showToast(R.string.sign_in_complete)
                is FirebaseResult.Fail -> setFireBaseError(
                    exception = it.exception,
                    updateEmailError = ::updateEmailError,
                    updatePasswordError = ::updatePasswordError,
                    showToast = ::showToast
                )
            }
        }
    }

    fun updatePasswordError(@StringRes error: Int?) =
        _uiState.update { it.copy(passwordError = error) }

    override fun updateEmailError(@StringRes error: Int?) =
        _uiState.update { it.copy(emailError = error) }

    override fun updateGoogleButtonEnabled(enabled: Boolean) =
        _uiState.update { it.copy(googleButtonEnabled = enabled) }

    override fun showToast(toastText: Int?) = _uiState.update { it.copy(toastText = toastText) }

    override fun toastShown() = _uiState.update { it.copy(toastText = null) }
}

data class SignInUIState(
    override val toastText: Int? = null,
    override val googleButtonEnabled: Boolean = false,
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null
) : GoogleSignInViewModelState() {
    override val isNotError get() = emailError != null && passwordError != null
}