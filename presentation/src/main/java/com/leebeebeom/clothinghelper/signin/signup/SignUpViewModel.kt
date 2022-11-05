package com.leebeebeom.clothinghelper.signin.signup

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUIState
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.PushInitialSubCategoriesFailed
import com.leebeebeom.clothinghelperdomain.usecase.signin.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState get() = _uiState.asStateFlow()

    fun signUpWithEmailAndPassword(email: String, name: String, password: String) =
        viewModelScope.launch {
            when (val authResult = signUpUseCase(email = email, password = password, name = name)) {
                is AuthResult.Success -> showToast(R.string.sign_up_complete)
                is AuthResult.Fail ->
                    if (authResult.exception?.message == PushInitialSubCategoriesFailed)
                        showToast(R.string.initial_sub_category_push_failed)
                    else setFireBaseError(
                        exception = authResult.exception,
                        updateEmailError = ::updateEmailError,
                        updatePasswordError = {},
                        showToast = ::showToast
                    )
            }
        }

    override fun updateGoogleButtonEnabled(enabled: Boolean) =
        _uiState.update { it.copy(googleButtonEnabled = enabled) }

    override fun showToast(toastText: Int?) = _uiState.update { it.copy(toastText = toastText) }

    override fun toastShown() = _uiState.update { it.copy(toastText = null) }

    override fun updateEmailError(error: Int?) = _uiState.update { it.copy(emailError = error) }
}

data class SignUpUIState(
    @StringRes val emailError: Int? = null,
    override val toastText: Int? = null,
    override val googleButtonEnabled: Boolean = true
) : GoogleSignInUIState() {
    override val isNotError get() = emailError == null
}