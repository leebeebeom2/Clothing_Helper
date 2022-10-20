package com.leebeebeom.clothinghelper.ui.signin.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.TAG
import com.leebeebeom.clothinghelper.ui.signin.base.BaseSignInUpViewModel
import com.leebeebeom.clothinghelper.ui.signin.base.BaseSignInUpViewModelState
import com.leebeebeom.clothinghelper.ui.signin.base.FirebaseErrorCode
import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    googleSignInUseCase: GoogleSignInUseCase
) : BaseSignInUpViewModel(googleSignInUseCase) {
    override val viewModelState = BaseSignInUpViewModelState()

    fun signUpWithEmailAndPassword(email: String, password: String, name: String) =
        viewModelScope.launch {
            signUpUseCase(
                email,
                password,
                name,
                signUpListener,
                updateNameListener
            )
        }

    private val signUpListener = object : FireBaseListeners.SignUpListener {
        override fun taskStart() = loadingOn()
        override fun taskSuccess() = showToast(R.string.sign_up_complete)
        override fun taskFailed(exception: Exception?) {
            val firebaseAuthException = exception as? FirebaseAuthException

            if (firebaseAuthException == null) {
                showToast(R.string.unknown_error)
                Log.d(TAG, "SignUpViewModel.taskFailed: firebaseAuthException = null")
            } else setError(firebaseAuthException.errorCode)
        }

        override fun taskFinish() = loadingOff()
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

    private val updateNameListener = object : FireBaseListeners.UpdateNameListener {
        override fun userNull() {
            showToast(R.string.unknown_error)
            Log.d(TAG, "updateName: user = null")
        }

        override fun nameUpdateFailed() = showToast(R.string.name_update_failed)

        override fun taskFailed(exception: Exception?) {
            val firebaseAuthException = exception as? FirebaseAuthException

            if (firebaseAuthException == null)
                Log.d(TAG, "taskFailed: firebaseAuthException = null")
            else Log.d(TAG, "taskFailed: firebaseAuthException = $firebaseAuthException")
            showToast(R.string.unknown_error)
        }

        override fun taskStart() = loadingOn()
        override fun taskSuccess() {}
        override fun taskFinish() = loadingOff()
    }
}