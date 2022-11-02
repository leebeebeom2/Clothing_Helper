package com.leebeebeom.clothinghelper.signin.signin

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldUIState
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInViewModelState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    override val uiState = SignInViewModelState()

    fun signInWithEmailAndPassword() {
        signInUseCase(uiState.email, uiState.password) {
            when (it) {
                is FirebaseResult.Success -> uiState.showToast(R.string.sign_in_complete)
                is FirebaseResult.Fail -> setFireBaseError(
                    exception = it.exception,
                    updateEmailError = uiState.emailState::updateError,
                    updatePasswordError = uiState.passwordState::updateError,
                    showToast = uiState::showToast
                )
            }
        }
    }
}

class SignInViewModelState : GoogleSignInViewModelState() {
    val emailState = MaxWidthTextFieldUIState()
    val passwordState = MaxWidthTextFieldUIState()

    val email get() = emailState.textFiled.text.trim()
    val password get() = passwordState.textFiled.text.trim()

    val signInButtonEnabled get() = email.isNotBlank() && password.isNotBlank() && !emailState.isError && !passwordState.isError
}