package com.leebeebeom.clothinghelper.signin.base

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.base.BaseViewModelState
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase

abstract class GoogleSignInUpViewModel(private val googleSignInUseCase: GoogleSignInUseCase) :
    ViewModel() {
    abstract val viewModelState: GoogleSignInViewModelState

    fun signInWithGoogleEmail(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            RESULT_OK -> googleSignInUseCase(
                credential = getGoogleCredential(activityResult)
            ) {
                when (it) {
                    is FirebaseResult.Success -> viewModelState.showToast(R.string.google_sign_in_complete)
                    is FirebaseResult.Fail -> {
                        viewModelState.showToast(R.string.unknown_error)
                        viewModelState.updateGoogleButtonEnabled(true)
                        Log.d(TAG, "taskFailed: ${it.exception}")
                    }
                }
            }
            RESULT_CANCELED -> {
                viewModelState.showToast(R.string.canceled)
                viewModelState.updateGoogleButtonEnabled(false)
            }
            else -> {
                viewModelState.showToast(R.string.unknown_error)
                Log.d(
                    TAG,
                    "GoogleSignInUpViewModel.signInWithGoogleEmail: resultCode = ${activityResult.resultCode}"
                )
                viewModelState.updateGoogleButtonEnabled(false)
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
}

open class GoogleSignInViewModelState : BaseViewModelState() {
    var googleButtonEnabled by mutableStateOf(true)
        private set

    fun updateGoogleButtonEnabled(enabled: Boolean) {
        googleButtonEnabled = enabled
    }
}