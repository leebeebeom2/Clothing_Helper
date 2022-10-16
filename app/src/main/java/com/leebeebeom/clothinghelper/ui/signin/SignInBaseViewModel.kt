package com.leebeebeom.clothinghelper.ui.signin

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