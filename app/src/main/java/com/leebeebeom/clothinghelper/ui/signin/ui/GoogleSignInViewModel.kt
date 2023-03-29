package com.leebeebeom.clothinghelper.ui.signin.ui

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.ui.viewmodel.LoadingViewModel
import com.leebeebeom.clothinghelper.util.buildConfigLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * Google 로그인 로직
 */
abstract class GoogleSignInViewModel(private val googleSignInUseCase: GoogleSignInUseCase) :
    LoadingViewModel(initialLoading = false) {

    fun signInWithGoogleEmail(
        activityResult: ActivityResult, googleSignInButtonEnable: () -> Unit
    ) {
        when (activityResult.resultCode) {
            Activity.RESULT_OK -> googleSignIn(
                activityResult = activityResult, googleSignInButtonEnable = googleSignInButtonEnable
            )
            Activity.RESULT_CANCELED -> {
                addToastTextAtLast(R.string.canceled)
                googleSignInButtonEnable()
            }
            else -> {
                buildConfigLog(
                    site = "signInWithGoogleEmail",
                    msg = "resultCode = ${activityResult.resultCode}",
                )
                addToastTextAtLast(R.string.unknown_error)
                googleSignInButtonEnable()
            }
        }
    }

    private fun googleSignIn(activityResult: ActivityResult, googleSignInButtonEnable: () -> Unit) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            buildConfigLog("GoogleSignInViewModel", "$throwable")
            addToastTextAtLast(R.string.unknown_error)
            isLoadingState = false
            googleSignInButtonEnable()
        }

        viewModelScope.launch(handler) {
            isLoadingState = true
            googleSignInUseCase.googleSignIn(credential = getGoogleCredential(activityResult = activityResult))
        }
    }

    private fun getGoogleCredential(activityResult: ActivityResult): AuthCredential {
        val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            .getResult(ApiException::class.java)
        return GoogleAuthProvider.getCredential(account.idToken, null)
    }
}