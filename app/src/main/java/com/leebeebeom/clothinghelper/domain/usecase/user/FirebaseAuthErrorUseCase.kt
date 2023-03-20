package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorCode.ERROR_WRONG_PASSWORD
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import com.leebeebeom.clothinghelper.util.buildConfigLog
import javax.inject.Inject

object FirebaseAuthErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
}

class FirebaseAuthErrorUseCase @Inject constructor() {
    fun firebaseAuthError(
        throwable: Throwable,
        setEmailError: (error: Int) -> Unit = {},
        setPasswordError: (error: Int) -> Unit = {},
        showToast: ShowToast,
    ) {
        when (throwable) {
            is FirebaseAuthException -> {
                when (throwable.errorCode) {
                    ERROR_INVALID_EMAIL -> setEmailError(R.string.error_invalid_email)
                    ERROR_EMAIL_ALREADY_IN_USE -> setEmailError(R.string.error_email_already_in_use)
                    ERROR_USER_NOT_FOUND -> setEmailError(R.string.error_user_not_found)
                    ERROR_WRONG_PASSWORD -> setPasswordError(R.string.error_wrong_password)
                }
            }
            is FirebaseTooManyRequestsException -> showToast(R.string.too_many_requst_error)
            is FirebaseNetworkException -> showToast(R.string.network_error)
            else -> {
                showToast(R.string.unknown_error)
                buildConfigLog(site = "firebaseAuthError", throwable = throwable)
            }
        }
    }
}
