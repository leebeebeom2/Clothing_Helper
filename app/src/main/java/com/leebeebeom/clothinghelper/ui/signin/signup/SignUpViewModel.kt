package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.UserRepository
import com.leebeebeom.clothinghelper.ui.signin.base.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.base.GoogleSignInImpl
import com.leebeebeom.clothinghelper.ui.signin.base.GoogleViewModelState
import com.leebeebeom.clothinghelper.ui.signin.base.SignInBaseViewModel

class SignUpViewModel : SignInBaseViewModel(), GoogleSignInImpl {
    val viewModelState by mutableStateOf(GoogleViewModelState())

    fun signUpWithEmailAndPassword(
        email: String, password: String, name: String, emailErrorEnabled: (Int) -> Unit
    ) {
        viewModelState.loadingOn()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(R.string.sign_up_complete)
                    if (it.result.user == null) showToast(R.string.name_update_failed)
                    else userNameUpdate(it.result.user!!, name)
                } else fireBaseErrorCheck(it.exception, emailErrorEnabled) {}
            }
    }

    private fun userNameUpdate(user: FirebaseUser, name: String) {
        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) UserRepository.userNameUpdate(name)
            else showToast(R.string.name_update_failed)
            viewModelState.loadingOff()
        }
    }

    override fun showToast(toastText: Int) = viewModelState.showToast(toastText)

    override fun setError(
        errorCode: String, emailErrorEnabled: (Int) -> Unit, passwordErrorEnabled: (Int) -> Unit
    ) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> emailErrorEnabled(R.string.error_invalid_email)
            FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE -> emailErrorEnabled(R.string.error_email_already_in_use)
            else -> showToast(R.string.unknown_error)
        }
        viewModelState.loadingOff()
    }

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