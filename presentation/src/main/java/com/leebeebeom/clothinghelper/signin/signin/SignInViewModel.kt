package com.leebeebeom.clothinghelper.signin.signin

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.BaseSignInUpViewModelState
import com.leebeebeom.clothinghelper.signin.base.FirebaseErrorCode
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
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
        loadingOn()
        signInUseCase(email, password, signInListener)
    }

    private val signInListener = object : FirebaseListener {
        override fun taskSuccess() = showToast(R.string.sign_in_complete)

        override fun taskFailed(exception: Exception?) {
            val firebaseAuthException = exception as? FirebaseAuthException

            if (firebaseAuthException == null) {
                showToast(R.string.unknown_error)
                Log.d(TAG, "SignInViewModel.taskFailed: firebaseAuthException = null")
            } else setError(firebaseAuthException.errorCode)
            loadingOff()
        }
    }

    private fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL ->
                viewModelState.showEmailError(R.string.error_invalid_email)
            FirebaseErrorCode.ERROR_USER_NOT_FOUND ->
                viewModelState.showEmailError(R.string.error_user_not_found)
            FirebaseErrorCode.ERROR_WRONG_PASSWORD ->
                viewModelState.showPasswordError(R.string.error_wrong_password)
            else -> {
                showToast(R.string.unknown_error)
                Log.d(TAG, "setError: $errorCode")
            }
        }
    }
}

class SignInViewModelState : BaseSignInUpViewModelState() {
    var passwordError: Int? by mutableStateOf(null)
        private set

    fun showPasswordError(@StringRes error: Int) {
        passwordError = error
    }

    fun hidePasswordError() {
        passwordError = null
    }
}