package com.leebeebeom.clothinghelper.signin.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.BaseSignInUpUIStates
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.PushInitialSubCategoriesFailed
import com.leebeebeom.clothinghelperdomain.usecase.signin.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    val uiStates = SignUpUIStates()

    fun signUpWithEmailAndPassword() = viewModelScope.launch {
        when (val result = signUpUseCase(
            email = uiStates.email, password = uiStates.password, name = uiStates.name
        )) {
            is AuthResult.Success -> showToast(R.string.sign_up_complete)
            is AuthResult.Fail -> if (result.exception?.message == PushInitialSubCategoriesFailed) showToast(
                R.string.initial_sub_category_push_failed
            )
            else setFireBaseError(
                exception = result.exception,
                updateEmailError = uiStates::updateEmailError,
                updatePasswordError = {},
                showToast = ::showToast
            )
        }
    }

    override fun updateGoogleButtonEnabled(enabled: Boolean) =
        uiStates.updateGoogleButtonEnabled(enabled)

    override fun showToast(text: Int) = uiStates.showToast(text)
}

class SignUpUIStates : BaseSignInUpUIStates() {
    var passwordConfirmError: Int? by mutableStateOf(null)
        private set

    var name = ""
        private set
    var passwordConfirm = ""
        private set

    fun onNameChange(name: String) {
        this.name = name
    }

    override fun onPasswordChange(password: String) {
        super.onPasswordChange(password)
        if (password.isNotBlank()) {
            if (password.length < 6) updatePasswordError(R.string.error_weak_password)
            if (passwordConfirm.isNotBlank() && password != passwordConfirm) updatePasswordConfirmError(
                R.string.error_password_confirm_not_same
            )
            else updatePasswordConfirmError(null)
        }
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        this.passwordConfirm = passwordConfirm
        if (passwordConfirm.isNotBlank() && password.isNotBlank() && passwordConfirm != password) updatePasswordConfirmError(
            R.string.error_password_confirm_not_same
        )
    }

    fun updatePasswordConfirmError(@StringRes error: Int?) {
        passwordConfirmError = error
    }

    override val buttonEnabled by derivedStateOf { super.buttonEnabled && passwordConfirm.isNotBlank() && this.passwordConfirmError == null }
}