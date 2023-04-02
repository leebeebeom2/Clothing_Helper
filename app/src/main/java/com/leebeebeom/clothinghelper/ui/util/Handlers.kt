package com.leebeebeom.clothinghelper.ui.util

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.TAG
import kotlinx.coroutines.CoroutineExceptionHandler

fun firebaseAuthErrorHandler(
    setEmailError: (Int) -> Unit,
    setPasswordError: (Int) -> Unit = {},
    showToast: ShowToast,
    loadingOff: () -> Unit
) = CoroutineExceptionHandler { _, throwable ->
    setFirebaseAuthError(
        throwable = throwable,
        setEmailError = setEmailError,
        setPasswordError = setPasswordError,
        showToast = showToast
    )
    loadingOff()
}

object FirebaseAuthErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
}

private fun setFirebaseAuthError(
    throwable: Throwable,
    setEmailError: (error: Int) -> Unit = {},
    setPasswordError: (error: Int) -> Unit = {},
    showToast: ShowToast,
) {
    when (throwable) {
        is FirebaseAuthException -> {
            when (throwable.errorCode) {
                FirebaseAuthErrorCode.ERROR_INVALID_EMAIL -> setEmailError(R.string.error_invalid_email)
                FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE -> setEmailError(R.string.error_email_already_in_use)
                FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND -> setEmailError(R.string.error_user_not_found)
                FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD -> setPasswordError(R.string.error_wrong_password)
            }
        }
        is FirebaseTooManyRequestsException -> showToast(R.string.error_too_many_requst)
        is FirebaseNetworkException -> showToast(R.string.network_error)
        else -> {
            showToast(R.string.unknown_error)
            Log.d(TAG, "setFirebaseAuthError: $throwable")
        }
    }
}

fun toastHandler(
    callSite: String, showToast: () -> Unit
) = CoroutineExceptionHandler { _, throwable ->
    Log.d(TAG, "$callSite: $throwable")
    showToast()
}
