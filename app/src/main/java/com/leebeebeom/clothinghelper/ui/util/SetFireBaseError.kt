package com.leebeebeom.clothinghelper.ui.util

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.A_NETWORK_ERROR
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import com.leebeebeom.clothinghelper.ui.util.FirebaseAuthErrorCode.TOO_MANY_REQUEST

object FirebaseAuthErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
    const val A_NETWORK_ERROR = "A_NETWORK_ERROR"
    const val TOO_MANY_REQUEST = "TOO_MANY_REQUEST"
}

fun fireBaseError(
    errorCode: String,
    updateEmailError: (error: Int) -> Unit,
    updatePasswordError: (error: Int) -> Unit = {},
    showToast: ShowToast
) {
    when (errorCode) {
        ERROR_INVALID_EMAIL -> updateEmailError(R.string.error_invalid_email)
        ERROR_EMAIL_ALREADY_IN_USE -> updateEmailError(R.string.error_email_already_in_use)
        ERROR_USER_NOT_FOUND -> updateEmailError(R.string.error_user_not_found)
        ERROR_WRONG_PASSWORD -> updatePasswordError(R.string.error_wrong_password)
        A_NETWORK_ERROR -> showToast(R.string.network_error)
        TOO_MANY_REQUEST -> showToast(R.string.too_many_requst_error)
        else -> {
            showToast(R.string.unknown_error)
            logE("setFirebaseError", Exception(errorCode))
        }
    }
}
