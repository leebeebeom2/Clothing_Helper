package com.leebeebeom.clothinghelper.signin.signin

import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.ToastUIState
import com.leebeebeom.clothinghelper.base.ToastUIStateImpl
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.interfaces.GoogleButtonUIState
import com.leebeebeom.clothinghelper.signin.base.interfaces.GoogleButtonUIStateImpl
import com.leebeebeom.clothinghelper.signin.base.interfaces.PasswordUIState
import com.leebeebeom.clothinghelper.signin.base.interfaces.PasswordUIStateImpl
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    val uiState = SignInUIState()

    fun signInWithEmailAndPassword() =
        viewModelScope.launch {
            when (val result = signInUseCase.signIn(uiState.email, uiState.password)) {
                is AuthResult.Success -> showToast(R.string.sign_in_complete)
                is AuthResult.Fail -> setFireBaseError(
                    errorCode = result.errorCode,
                    updateEmailError = uiState::updateEmailError,
                    updatePasswordError = uiState::updatePasswordError,
                    showToast = ::showToast
                )
                is AuthResult.UnknownFail -> showToast(R.string.unknown_error)
            }
        }

    override fun updateGoogleButtonEnabled(enabled: Boolean) =
        uiState.updateGoogleButtonEnabled(enabled)

    override fun showToast(text: Int) = uiState.showToast(text)
}

class SignInUIState :
    GoogleButtonUIState by GoogleButtonUIStateImpl(),
    ToastUIState by ToastUIStateImpl(),
    PasswordUIState by PasswordUIStateImpl()