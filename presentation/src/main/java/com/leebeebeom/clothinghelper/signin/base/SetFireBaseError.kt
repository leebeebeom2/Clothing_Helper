package com.leebeebeom.clothinghelper.signin.base

import android.util.Log
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG

object FirebaseErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
}

fun setFireBaseError(
    exception: Exception?,
    updateEmailError: (error: Int) -> Unit,
    updatePasswordError: (error: Int) -> Unit,
    showToast: (text: Int) -> Unit
) {
    val firebaseAuthException = exception as? FirebaseAuthException

    if (firebaseAuthException == null) {
        showToast(R.string.unknown_error)
        Log.d(TAG, "setFirebaseError: firebaseAuthExcaption = null")
    }
    else when (firebaseAuthException.errorCode) {
        FirebaseErrorCode.ERROR_INVALID_EMAIL -> updateEmailError(R.string.error_invalid_email)
        FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE -> updateEmailError(R.string.error_email_already_in_use)
        FirebaseErrorCode.ERROR_USER_NOT_FOUND -> updateEmailError(R.string.error_user_not_found)
        FirebaseErrorCode.ERROR_WRONG_PASSWORD -> updatePasswordError(R.string.error_wrong_password)
        else -> {
            showToast(R.string.unknown_error)
            Log.d(TAG, "setFirebaseError: ${firebaseAuthException.errorCode}")
        }
    }
}
