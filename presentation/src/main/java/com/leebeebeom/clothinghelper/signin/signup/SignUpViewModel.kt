package com.leebeebeom.clothinghelper.signin.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.interfaces.GoogleButtonUIState
import com.leebeebeom.clothinghelper.signin.base.interfaces.GoogleButtonUIStateImpl
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdata.repository.util.logE
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    val uiState = SignUpUIState()

    fun signUpWithEmailAndPassword() =
        viewModelScope.launch {
            val result = signUpUseCase.signUp(
                email = uiState.email,
                password = uiState.password,
                name = uiState.name
            ) {
                if (it is FirebaseResult.Fail) {
                    uiState.showToast(R.string.data_load_failed)
                    logE("signUpWithEmailAndPassword", it.exception)
                }
            }
            when (result) {
                is AuthResult.Success -> showToast(R.string.sign_up_complete)
                is AuthResult.Fail -> setFireBaseError(
                    errorCode = result.errorCode,
                    updateEmailError = uiState::updateEmailError,
                    updatePasswordError = {},
                    showToast = ::showToast
                )
                is AuthResult.UnknownFail -> showToast(R.string.unknown_error)
            }
        }

    override fun updateGoogleButtonEnabled(enabled: Boolean) =
        uiState.updateGoogleButtonEnabled(enabled)

    override fun showToast(text: Int) = uiState.showToast(text)
    override fun disableGoogleButton() = uiState.updateGoogleButtonEnabled(false)
}

class SignUpUIState(
    private val googleButtonUIStateImpl: GoogleButtonUIStateImpl = GoogleButtonUIStateImpl()
) : GoogleButtonUIState by googleButtonUIStateImpl {
    var passwordConfirmError: Int? by mutableStateOf(null)
        private set

    var name by mutableStateOf("")
        private set
    var passwordConfirm by mutableStateOf("")
        private set

    fun onNameChange(name: String) {
        this.name = name
    }

    override fun onPasswordChange(password: String) {
        googleButtonUIStateImpl.onPasswordChange(password)
        if (password.isNotBlank()) {
            if (password.length < 6) updatePasswordError(R.string.error_weak_password)
            if (passwordConfirm.isNotBlank() && password != passwordConfirm)
                updatePasswordConfirmError(R.string.error_password_confirm_not_same)
            else updatePasswordConfirmError(null)
        }
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        this.passwordConfirm = passwordConfirm
        if (passwordConfirm.isNotBlank() && password.isNotBlank() && passwordConfirm != password)
            updatePasswordConfirmError(R.string.error_password_confirm_not_same)
    }

    fun updatePasswordConfirmError(@StringRes error: Int?) {
        passwordConfirmError = error
    }

    override val buttonEnabled by derivedStateOf { googleButtonUIStateImpl.buttonEnabled && passwordConfirm.isNotBlank() && this.passwordConfirmError == null }
}