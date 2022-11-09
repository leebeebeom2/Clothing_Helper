package com.leebeebeom.clothinghelper.signin.signin

import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.BaseSignInUpUIStates
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

    val uiState = BaseSignInUpUIStates()

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