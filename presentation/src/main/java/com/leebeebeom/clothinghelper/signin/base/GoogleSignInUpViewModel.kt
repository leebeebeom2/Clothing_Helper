package com.leebeebeom.clothinghelper.signin.base

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.signin.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.PushInitialSubCategoriesFailed
import kotlinx.coroutines.launch

abstract class GoogleSignInUpViewModel(private val googleSignInUseCase: GoogleSignInUseCase) :
    BaseSignInViewModel() {

    abstract fun updateGoogleButtonEnabled(enabled: Boolean)

    fun signInWithGoogleEmail(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            RESULT_OK -> {
                viewModelScope.launch {
                    when (val result =
                        googleSignInUseCase(credential = getGoogleCredential(activityResult))) {
                        is AuthResult.Success -> showToast(R.string.google_sign_in_complete)
                        is AuthResult.Fail -> {
                            if (result.exception?.message == PushInitialSubCategoriesFailed)
                                showToast(R.string.initial_sub_category_push_failed)
                            else {
                                showToast(R.string.unknown_error)
                                Log.e(TAG, "signInWithGoogleEmail: $result")
                                updateGoogleButtonEnabled(enabled = true)
                            }
                        }
                    }
                }
            }
            RESULT_CANCELED -> {
                showToast(R.string.canceled)
                updateGoogleButtonEnabled(enabled = true)
            }
            else -> {
                Log.e(TAG, "signInWithGoogleEmail: resultCode = ${activityResult.resultCode}")
                showToast(R.string.unknown_error)
                updateGoogleButtonEnabled(enabled = true)
            }
        }
    }

    private fun getGoogleCredential(activityResult: ActivityResult): AuthCredential? {
        return if (activityResult.data == null) {
            showToast(R.string.unknown_error)
            updateGoogleButtonEnabled(enabled = true)
            Log.e(TAG, "getGoogleCredential: activityResult.data = null")
            null
        } else {
            val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)
            GoogleAuthProvider.getCredential(account.idToken, null)
        }
    }
}

abstract class GoogleSignInUIState : BaseSignInUIState() {
    abstract val googleButtonEnabled: Boolean
}