package com.leebeebeom.clothinghelper.signin.base

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener2
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase

abstract class GoogleSignInUpViewModel(private val googleSignInUseCase: GoogleSignInUseCase) :
    ViewModel() {
    abstract val viewModelState: GoogleSignInViewModelState

    // call from UI
    val taskStart = {
        viewModelState.loadingOn()
        viewModelState.setGoogleButtonDisable()
    }

    fun signInWithGoogleEmail(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            RESULT_OK -> googleSignInUseCase(
                credential = getGoogleCredential(activityResult),
                listener = listener
            )
            RESULT_CANCELED -> {
                viewModelState.showToast(R.string.canceled)
                listener.taskFailed(null)
                listener.taskFinish()
            }
            else -> {
                viewModelState.showToast(R.string.unknown_error)
                Log.d(
                    TAG,
                    "GoogleSignInUpViewModel.signInWithGoogleEmail: resultCode = ${activityResult.resultCode}"
                )
                listener.taskFailed(null)
                listener.taskFinish()
            }
        }
    }

    private fun getGoogleCredential(activityResult: ActivityResult): AuthCredential? {
        return if (activityResult.data == null) {
            viewModelState.showToast(R.string.unknown_error)
            Log.d(TAG, "BaseSignInUpViewModel.getGoogleCredential: activityResult.data = null")
            null
        } else {
            val account = GoogleSignIn
                .getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)
            GoogleAuthProvider.getCredential(account.idToken, null)
        }
    }

    private val listener = object : FirebaseListener2 {
        override fun taskSuccess() {
            viewModelState.showToast(R.string.google_sign_in_complete)
        }

        override fun taskFailed(exception: Exception?) {
            viewModelState.showToast(R.string.unknown_error)
            viewModelState.setGoogleButtonEnable()
            Log.d(TAG, "taskFailed: $exception")
        }

        override fun taskFinish() {
            viewModelState.loadingOff()
        }
    }
}