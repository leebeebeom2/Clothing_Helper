package com.leebeebeom.clothinghelper.signin.base

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase

object FirebaseErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
}

abstract class GoogleSignInUpViewModel(
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {
    abstract val viewModelState: BaseSignInUpViewModelState

    fun showToast(@StringRes toastText: Int) = viewModelState.showToast(toastText)
    fun loadingOn() = viewModelState.loadingOn()
    fun loadingOff() = viewModelState.loadingOff()
    private fun googleButtonDisable() = viewModelState.setGoogleButtonDisable()
    private fun googleButtonEnable() = viewModelState.setGoogleButtonEnable()

    fun taskStart() {
        loadingOn()
        googleButtonDisable()
    }

    private fun taskFinish() {
        loadingOff()
        googleButtonEnable()
    }

    fun signInWithGoogleEmail(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            RESULT_OK -> googleSignInUseCase(
                getGoogleCredential(activityResult),
                googleSignInListener
            )
            RESULT_CANCELED -> {
                showToast(R.string.canceled)
                taskFinish()
            }
            else -> {
                showToast(R.string.unknown_error)
                Log.d(
                    TAG,
                    "BaseSignInUpViewModel.signInWithGoogleEmail: resultCode = ${activityResult.resultCode}"
                )
            }
        }
        taskFinish()
    }

    private fun getGoogleCredential(activityResult: ActivityResult): AuthCredential? {
        return if (activityResult.data == null) {
            showToast(R.string.unknown_error)
            Log.d(TAG, "BaseSignInUpViewModel.getGoogleCredential: activityResult.data = null")
            null
        } else {
            val account = GoogleSignIn
                .getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)
            GoogleAuthProvider.getCredential(account.idToken, null)
        }
    }

    private val googleSignInListener = object : FirebaseListener {
        override fun taskSuccess() = showToast(R.string.google_sign_in_complete)

        override fun taskFailed(exception: Exception?) {
            showToast(R.string.unknown_error)
            Log.d(TAG, "taskFailed: $exception")
        }
    }
}