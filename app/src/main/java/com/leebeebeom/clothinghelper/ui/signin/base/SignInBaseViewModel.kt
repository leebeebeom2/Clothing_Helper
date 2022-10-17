package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R

abstract class SignInBaseViewModel : ViewModel() {
    abstract fun showToast(@StringRes toastText: Int)
    abstract fun setError(
        errorCode: String,
        emailErrorEnabled: (Int) -> Unit,
        passwordErrorEnabled: (Int) -> Unit
    )

    protected fun fireBaseErrorCheck(
        exception: Exception?,
        setEmailError: (Int) -> Unit,
        setPasswordError: (Int) -> Unit
    ) {
        val firebaseException = (exception as? FirebaseAuthException)

        if (firebaseException == null) showToast(R.string.unknown_error)
        else setError(firebaseException.errorCode, setEmailError, setPasswordError)
    }
}

object FirebaseErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
}