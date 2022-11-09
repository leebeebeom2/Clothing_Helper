package com.leebeebeom.clothinghelper.signin.signin

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    val uiState = SignInUIStates()

    fun signInWithEmailAndPassword() =
        viewModelScope.launch {

            when (val result = signInUseCase(uiState.email, uiState.password)) {
                is AuthResult.Success -> showToast(R.string.sign_in_complete)
                is AuthResult.Fail -> setFireBaseError(
                    exception = result.exception,
                    updateEmailError = uiState::updateEmailError,
                    updatePasswordError = uiState::updatePasswordError,
                    showToast = ::showToast
                )
                else -> showToast(R.string.unknown_error)
            }
        }

    override fun updateGoogleButtonEnabled(enabled: Boolean) =
        uiState.updateGoogleButtonEnabled(enabled)

    override fun showToast(text: Int) = uiState.showToast(text)
}

data class SignInUIStates(
    @StringRes private val _toastText: MutableState<Int?> = mutableStateOf(null),
    private val _googleButtonEnabled: MutableState<Boolean> = mutableStateOf(false),
    @StringRes private val _emailError: MutableState<Int?> = mutableStateOf(null),
    @StringRes private val _passwordError: MutableState<Int?> = mutableStateOf(null)
) {
    val toastText by derivedStateOf { _toastText.value }
    val googleButtonEnabled by derivedStateOf { _googleButtonEnabled.value }
    val emailError by derivedStateOf { _emailError.value }
    val passwordError by derivedStateOf { _passwordError.value }

    var email = ""
        private set
    var password = ""
        private set

    fun updateEmailError(@StringRes error: Int?) {
        _emailError.value = error
    }

    fun updatePasswordError(@StringRes error: Int?) {
        _passwordError.value = error
    }

    fun updateGoogleButtonEnabled(enabled: Boolean) {
        _googleButtonEnabled.value = enabled
    }

    fun showToast(@StringRes text: Int?) {
        _toastText.value = text
    }

    fun toastShown() {
        _toastText.value = null
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    fun onPasswordChange(email: String) {
        this.password = email
    }

    val signInButtonEnabled by derivedStateOf {
        emailError == null && passwordError == null && email.isNotBlank() && password.isNotBlank()
    }
}
