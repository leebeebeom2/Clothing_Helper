package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.ui.signin.base.setFireBaseError
import com.leebeebeom.clothinghelper.ui.signin.state.GoogleButtonUIState
import com.leebeebeom.clothinghelper.ui.signin.state.GoogleButtonUIStateImpl
import com.leebeebeom.clothinghelper.ui.signin.state.PasswordUIState
import com.leebeebeom.clothinghelper.ui.signin.state.PasswordUIStateImpl
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

    override val uiState = SignInUIState()

    fun signInWithEmailAndPassword() =
        viewModelScope.launch {
            when (val result = signInUseCase.signIn(uiState.email, uiState.password)) {
                is AuthResult.Success -> uiState.showToast(R.string.sign_in_complete)
                is AuthResult.Fail -> setFireBaseError(
                    errorCode = result.errorCode,
                    updateEmailError = uiState::updateEmailError,
                    updatePasswordError = uiState::updatePasswordError,
                    showToast = uiState::showToast
                )
                is AuthResult.UnknownFail -> uiState.showToast(R.string.unknown_error)
            }
        }
}

class SignInUIState :
    GoogleButtonUIState by GoogleButtonUIStateImpl(),
    PasswordUIState by PasswordUIStateImpl()