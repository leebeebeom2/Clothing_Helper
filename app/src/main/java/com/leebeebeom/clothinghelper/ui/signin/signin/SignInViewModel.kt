package com.leebeebeom.clothinghelper.ui.signin.signin

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel
import com.leebeebeom.clothinghelper.ui.signin.base.BaseViewModelState

class SignInViewModel : SignInBaseViewModel(), GoogleSignInImpl {
    val viewModelState by mutableStateOf(SignInViewModelState())

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        emailErrorEnabled: (Int) -> Unit,
        passwordErrorEnabled: (Int) -> Unit,
    ) {
        viewModelState.loadingOn()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) showToast(R.string.login_complete)
                else fireBaseErrorCheck(it.exception, emailErrorEnabled, passwordErrorEnabled)
                viewModelState.loadingOff()
            }
    }

    override fun setError(
        errorCode: String,
        emailErrorEnabled: (Int) -> Unit,
        passwordErrorEnabled: (Int) -> Unit
    ) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL ->
                emailErrorEnabled(R.string.error_invalid_email)
            FirebaseErrorCode.ERROR_USER_NOT_FOUND ->
                emailErrorEnabled(R.string.error_user_not_found)
            FirebaseErrorCode.ERROR_WRONG_PASSWORD ->
                passwordErrorEnabled(R.string.error_wrong_password)
            else -> showToast(R.string.unknown_error)
        }
    }

    override fun showToast(@StringRes toastText: Int) = viewModelState.showToast(toastText)

    override fun googleSignInTaskFinished(@StringRes toastText: Int) {
        showToast(toastText)
        viewModelState.loadingOff()
        viewModelState.googleButtonEnable()
    }

    override fun googleSignInTaskStart() {
        viewModelState.loadingOn()
        viewModelState.googleButtonDisEnable()
    }
}

class SignInViewModelState : BaseViewModelState() {
    var googleButtonEnabled: Boolean by mutableStateOf(true)
        private set

    fun googleButtonEnable() {
        googleButtonEnabled = true
    }

    fun googleButtonDisEnable() {
        googleButtonEnabled = false
    }
}