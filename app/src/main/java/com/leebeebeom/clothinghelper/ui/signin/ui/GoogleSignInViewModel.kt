package com.leebeebeom.clothinghelper.ui.signin.ui

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.ui.ToastViewModel
import com.leebeebeom.clothinghelper.util.buildConfigLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Google 로그인 로직
 */
abstract class GoogleSignInViewModel(private val googleSignInUseCase: GoogleSignInUseCase) :
    ToastViewModel() {
    abstract val googleSignInState: GoogleSignInState

    fun signInWithGoogleEmail(activityResult: ActivityResult) =
        when (activityResult.resultCode) {
            Activity.RESULT_OK -> googleSignIn(activityResult = activityResult)
            Activity.RESULT_CANCELED -> {
                addToastTextAtLast(R.string.canceled)
                googleSignInState.googleButtonEnabled()
                null
            }
            else -> {
                buildConfigLog(
                    site = "signInWithGoogleEmail",
                    msg = "resultCode = ${activityResult.resultCode}",
                )
                addToastTextAtLast(R.string.unknown_error)
                googleSignInState.googleButtonEnabled()
                null
            }
        }

    private fun googleSignIn(activityResult: ActivityResult): Job {
        val handler = CoroutineExceptionHandler { _, throwable ->
            buildConfigLog("GoogleSignInViewModel", "$throwable")
            addToastTextAtLast(R.string.unknown_error)
            googleSignInState.setLoading(false)
            googleSignInState.googleButtonEnabled()
        }

        return viewModelScope.launch(handler) {
            googleSignInState.setLoading(true)
            googleSignInUseCase.googleSignIn(credential = getGoogleCredential(activityResult = activityResult))
        }
    }

    private fun getGoogleCredential(activityResult: ActivityResult): AuthCredential {
        val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            .getResult(ApiException::class.java)
        return GoogleAuthProvider.getCredential(account.idToken, null)
    }
}

abstract class GoogleSignInUiState {
    abstract val googleButtonEnabled: Boolean
    abstract val isLoading: Boolean
}

abstract class GoogleSignInState(
    savedStateHandle: SavedStateHandle,
    savedEmailKey: String,
    emailErrorKey: String,
    savedPasswordKey: String,
    passwordErrorKey: String
) : PasswordState(
    savedStateHandle = savedStateHandle,
    emailKey = savedEmailKey,
    emailErrorKey = emailErrorKey,
    passwordKey = savedPasswordKey,
    passwordErrorKey = passwordErrorKey
) {
    private var googleButtonEnabledState by mutableStateOf(true)
    val googleButtonEnabledFlow = snapshotFlow { googleButtonEnabledState }

    fun googleButtonEnabled() {
        googleButtonEnabledState = true
    }

    fun googleButtonDisable() {
        googleButtonEnabledState = false
    }
}