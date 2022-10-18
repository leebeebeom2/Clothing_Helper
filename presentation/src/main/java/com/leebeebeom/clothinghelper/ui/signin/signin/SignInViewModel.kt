package com.leebeebeom.clothinghelper.ui.signin.signin

import android.util.Log
import androidx.activity.result.ActivityResult
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.repository.FireBaseListeners
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignInUseCase
import com.leebeebeom.clothinghelper.ui.TAG
import com.leebeebeom.clothinghelper.ui.signin.base.BaseSignInUpViewModel
import com.leebeebeom.clothinghelper.ui.signin.base.FirebaseErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
) : BaseSignInUpViewModel() {

    fun signInWithEmailAndPassword(email: String, password: String) =
        signInUseCase(email, password, signInListener)

    fun signInWithGoogleEmail(activityResult: ActivityResult) =
        googleSignInUseCase(activityResult, googleSignInListener)

    private val signInListener = object : FireBaseListeners.SignInListener {
        override fun taskStart() = viewModelState.loadingOn()

        override fun taskSuccess() = showToast(R.string.login_complete)

        override fun taskFailed(exception: Exception?) {
            val firebaseException = exception as? FirebaseAuthException

            if (firebaseException == null) {
                showToast(R.string.unknown_error)
                Log.d(TAG, "taskFailed: firebaseException = null")
            } else setError(firebaseException.errorCode)
        }

        override fun taskFinish() = viewModelState.loadingOff()
    }

    private fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL ->
                viewModelState.emailErrorOn(R.string.error_invalid_email)
            FirebaseErrorCode.ERROR_USER_NOT_FOUND ->
                viewModelState.emailErrorOn(R.string.error_user_not_found)
            FirebaseErrorCode.ERROR_WRONG_PASSWORD ->
                viewModelState.passwordErrorOn(R.string.error_wrong_password)
            else -> {
                showToast(R.string.unknown_error)
                Log.d(TAG, "setError: $errorCode")
            }
        }
    }
}