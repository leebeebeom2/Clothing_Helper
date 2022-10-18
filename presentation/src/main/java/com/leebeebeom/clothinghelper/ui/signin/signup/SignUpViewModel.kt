package com.leebeebeom.clothinghelper.ui.signin.signup

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.repository.FireBaseListeners
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignUpUseCase
import com.leebeebeom.clothinghelper.ui.TAG
import com.leebeebeom.clothinghelper.ui.signin.base.BaseSignInUpViewModel
import com.leebeebeom.clothinghelper.ui.signin.base.BaseSignInUpViewModelState
import com.leebeebeom.clothinghelper.ui.signin.base.FirebaseErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    googleSignInUseCase: GoogleSignInUseCase
) : BaseSignInUpViewModel(googleSignInUseCase) {
    override val viewModelState = SignUpViewModelState()

    fun signUpWithEmailAndPassword(email: String, password: String, name: String) =
        signUpUseCase(email, password, name, signUpListener, updateNameListener)

    private val signUpListener = object : FireBaseListeners.SignUpListener {
        override fun taskStart() = loadingOn()

        override fun taskSuccess() = showToast(R.string.sign_up_complete)

        override fun taskFailed(exception: Exception?) {
            val firebaseException = exception as? FirebaseAuthException

            if (firebaseException == null) {
                showToast(R.string.unknown_error)
                Log.d(TAG, "taskFailed: firebaseException = null")
            } else setError(firebaseException.errorCode)
        }

        override fun taskFinish() = loadingOff()
    }

    private val updateNameListener = object : FireBaseListeners.UpdateNameListener {
        override fun userNull() {
            showToast(R.string.unknown_error)
            Log.d(TAG, "updateName: user = null")
        }

        override fun nameUpdateFailed() = showToast(R.string.name_update_failed)

        override fun taskFailed(exception: Exception?) {
            val firebaseAuthException = exception as? FirebaseAuthException

            if (firebaseAuthException == null) {
                showToast(R.string.unknown_error)
                Log.d(TAG, "taskFailed: firebaseAuthException = null")
            } else setError(firebaseAuthException.errorCode)
        }

        override fun taskStart() = loadingOn()

        override fun taskSuccess() {
            //TODO UserStateUseCase에서 이름 업데이트
        }

        override fun taskFinish() = loadingOff()

    }

    private fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> viewModelState.emailErrorOn(R.string.error_invalid_email)
            FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE -> viewModelState.emailErrorOn(R.string.error_email_already_in_use)
            else -> {
                showToast(R.string.unknown_error)
                Log.d(TAG, "setError: $errorCode")
            }
        }
    }
}

class SignUpViewModelState : BaseSignInUpViewModelState() {
    var passwordConfirmError: Int? by mutableStateOf(null)
        private set

    fun passwordConfirmErrorOn(@StringRes error: Int) {
        passwordConfirmError = error
    }

    fun passwordConfirmErrorOff() {
        passwordConfirmError = null
    }
}