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
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase

abstract class GoogleSignInUpViewModel(private val googleSignInUseCase: GoogleSignInUseCase) :
    ViewModel() {
    abstract val viewModelState: GoogleSignInViewModelState

    // call from UI
    val taskStart = {
        viewModelState.loadingOn()
        viewModelState.setGoogleButtonDisable()
    }

    val taskFinish = {
        viewModelState.loadingOff()
        viewModelState.setGoogleButtonEnable()
    }

    fun signInWithGoogleEmail(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            RESULT_OK -> googleSignInUseCase(
                googleCredential = getGoogleCredential(activityResult),
                googleSignInListener = googleSignInListener, taskFinish = taskFinish
            )
            RESULT_CANCELED -> {
                viewModelState.showToast(R.string.canceled)
                taskFinish()
            }
            else -> {
                viewModelState.showToast(R.string.unknown_error)
                Log.d(
                    TAG,
                    "GoogleSignInUpViewModel.signInWithGoogleEmail: resultCode = ${activityResult.resultCode}"
                )
                taskFinish()
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

    private val googleSignInListener = object : FirebaseListener {
        override fun taskSuccess() {
            viewModelState.showToast(R.string.google_sign_in_complete)
            viewModelState.loadingOff()
        }

        override fun taskFailed(exception: Exception?) {
            viewModelState.showToast(R.string.unknown_error)
            taskFinish()
            Log.d(TAG, "taskFailed: $exception")
        }
    }
}