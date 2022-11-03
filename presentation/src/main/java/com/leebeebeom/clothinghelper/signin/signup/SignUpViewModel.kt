package com.leebeebeom.clothinghelper.signin.signup

import android.util.Log
import androidx.annotation.StringRes
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUIState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState get() = _uiState.asStateFlow()

    fun signUpWithEmailAndPassword(email: String, name: String, password: String) {
        signUpUseCase(
            email = email,
            password = password,
            name = name,
            onSignUpDone = {
                when (it) {
                    is FirebaseResult.Success -> showToast(R.string.sign_up_complete)
                    is FirebaseResult.Fail -> {
                        setFireBaseError(
                            exception = it.exception,
                            updateEmailError = ::updateEmailError,
                            updatePasswordError = {},
                            showToast = ::showToast
                        )
                    }
                }
            }
        ) {
            if (it is FirebaseResult.Fail) {
                showToast(R.string.name_update_failed)
                Log.d(TAG, "taskFailed: $it.exception")
            }
        }
    }


    override fun updateGoogleButtonEnabled(enabled: Boolean) =
        _uiState.update { it.copy(googleButtonEnabled = enabled) }

    override fun showToast(toastText: Int?) =
        _uiState.update { it.copy(toastText = toastText) }

    override fun toastShown() =
        _uiState.update { it.copy(toastText = null) }

    override fun updateEmailError(error: Int?) =
        _uiState.update { it.copy(emailError = error) }

    fun updatePasswordError(error: Int?) =
        _uiState.update { it.copy(passwordError = error) }

    fun updatePasswordConfirmError(error: Int?) =
        _uiState.update { it.copy(passwordConfirmError = error) }
}

data class SignUpUIState(
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    @StringRes val passwordConfirmError: Int? = null,
    override val toastText: Int? = null,
    override val googleButtonEnabled: Boolean = true
) : GoogleSignInUIState() {
    override val isNotError get() = emailError == null && passwordError == null && passwordConfirmError == null
}