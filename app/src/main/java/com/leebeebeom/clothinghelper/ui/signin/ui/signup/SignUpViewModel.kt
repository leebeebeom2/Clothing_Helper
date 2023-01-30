package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.AuthResult.*
import com.leebeebeom.clothinghelper.domain.usecase.user.SignUpUseCase
import com.leebeebeom.clothinghelper.ui.signin.base.EmailAndPasswordViewModel
import com.leebeebeom.clothinghelper.ui.signin.state.EmailAndPasswordState
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailAndPasswordUiState
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import com.leebeebeom.clothinghelper.ui.util.fireBaseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : EmailAndPasswordViewModel() {

    override val mutableUiState: MutableSignUpUiState = MutableSignUpUiState()
    val uiState: SignUpUiState = mutableUiState

    fun onNameChange(name: String) {
        mutableUiState.name = name
    }

    override fun onPasswordChange(password: String) {
        super.onPasswordChange(password)

        if (password.isBlank()) return

        if (password.length < 6) mutableUiState.passwordError = R.string.error_weak_password

        if (mutableUiState.passwordConfirm.isBlank()) return

        if (password != mutableUiState.passwordConfirm)
            mutableUiState.passwordConfirmError = R.string.error_password_confirm_not_same
        else mutableUiState.passwordConfirmError = null
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        mutableUiState.passwordConfirm = passwordConfirm
        mutableUiState.passwordConfirmError = null

        if (passwordConfirm.isBlank()) return

        if (passwordConfirm != mutableUiState.password)
            mutableUiState.passwordConfirmError = R.string.error_password_confirm_not_same
    }

    fun signUpWithEmailAndPassword(showToast: ShowToast) {
        viewModelScope.launch {
            val result = signUpUseCase.signUp(email = mutableUiState.email,
                password = mutableUiState.password,
                name = mutableUiState.name,
                onSubCategoryLoadFail = { showToast(R.string.data_load_failed) })
            when (result) {
                is Success -> showToast(R.string.sign_up_complete)
                is Fail -> fireBaseError(
                    errorCode = result.errorCode,
                    updateEmailError = { mutableUiState.emailError = it },
                    showToast = showToast
                )
                is UnknownFail -> showToast(R.string.unknown_error)
            }
        }
    }
}

@Stable
interface SignUpUiState : EmailAndPasswordState {
    val passwordConfirmError: Int?
}

class MutableSignUpUiState : MutableEmailAndPasswordUiState(), SignUpUiState {
    override var passwordConfirmError: Int? by mutableStateOf(null)

    var name = ""
    var passwordConfirm = ""

    override val buttonEnabled by derivedStateOf {
        super.buttonEnabled && name.isNotBlank() && passwordConfirm.isNotBlank() && passwordConfirmError == null
    }
}