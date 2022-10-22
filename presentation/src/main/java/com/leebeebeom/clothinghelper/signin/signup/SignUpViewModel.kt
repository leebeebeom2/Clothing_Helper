package com.leebeebeom.clothinghelper.signin.signup

import android.util.Log
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.BaseSignInUpViewModelState
import com.leebeebeom.clothinghelper.signin.base.FirebaseErrorCode
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {
    override val viewModelState = BaseSignInUpViewModelState()

    fun signUpWithEmailAndPassword(email: String, password: String, name: String) {
        loadingOn()
        signUpUseCase(
            email,
            password,
            name,
            signUpListener,
            updateNameListener
        )
    }

    private val signUpListener = object : FirebaseListener {
        override fun taskSuccess() = showToast(R.string.sign_up_complete)
        override fun taskFailed(exception: Exception?) {
            val firebaseAuthException = exception as? FirebaseAuthException

            if (firebaseAuthException == null) {
                showToast(R.string.unknown_error)
                Log.d(TAG, "SignUpViewModel.taskFailed: firebaseAuthException = null")
            } else setError(firebaseAuthException.errorCode)
            loadingOff()
        }
    }

    private fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> viewModelState.showEmailError(R.string.error_invalid_email)
            FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE -> viewModelState.showEmailError(R.string.error_email_already_in_use)
            else -> {
                showToast(R.string.unknown_error)
                Log.d(TAG, "setError: $errorCode")
            }
        }
    }

    private val updateNameListener = object : FirebaseListener {
        override fun taskFailed(exception: Exception?) {
            showToast(R.string.name_update_failed)
        }

        override fun taskSuccess() {}
    }
}