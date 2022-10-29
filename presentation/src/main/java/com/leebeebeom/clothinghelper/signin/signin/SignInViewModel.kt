package com.leebeebeom.clothinghelper.signin.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInViewModelState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener2
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    override val viewModelState = SignInViewModelState()

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelState.loadingOn()
        signInUseCase(email, password, signInListener)
    }

    private val signInListener = object : FirebaseListener2 {
        override fun taskSuccess() = viewModelState.showToast(R.string.sign_in_complete)

        override fun taskFailed(exception: Exception?) =
            setFireBaseError(
                exception = exception,
                showEmailError = viewModelState.showEmailError,
                showPasswordError = viewModelState.showPasswordError,
                showToast = viewModelState.showToast
            )

        override fun taskFinish() = viewModelState.loadingOff()
    }
}

class SignInViewModelState : GoogleSignInViewModelState() {
    var passwordError: Int? by mutableStateOf(null)
        private set

    val showPasswordError = { error: Int -> passwordError = error }
    val hidePasswordError = { passwordError = null }
}