package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.AuthResult.*
import com.leebeebeom.clothinghelper.domain.usecase.user.SignInUseCase
import com.leebeebeom.clothinghelper.ui.signin.base.EmailAndPasswordViewModel
import com.leebeebeom.clothinghelper.ui.signin.state.EmailAndPasswordState
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailAndPasswordUiState
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import com.leebeebeom.clothinghelper.ui.util.fireBaseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val signInUseCase: SignInUseCase) :
    EmailAndPasswordViewModel() {

    override val mutableUiState: MutableEmailAndPasswordUiState = MutableEmailAndPasswordUiState()
    val uiState: EmailAndPasswordState = mutableUiState

    fun signInWithEmailAndPassword(showToast: ShowToast) =
        viewModelScope.launch {
            when (val result =
                signInUseCase.signIn(
                    email = mutableUiState.email,
                    password = mutableUiState.password
                )) {
                is Success -> showToast(R.string.sign_in_complete)
                is Fail -> fireBaseError(
                    errorCode = result.errorCode,
                    updateEmailError = { mutableUiState.emailError = it },
                    updatePasswordError = { mutableUiState.passwordError = it },
                    showToast = showToast
                )
                is UnknownFail -> showToast(R.string.unknown_error)
            }
        }
}