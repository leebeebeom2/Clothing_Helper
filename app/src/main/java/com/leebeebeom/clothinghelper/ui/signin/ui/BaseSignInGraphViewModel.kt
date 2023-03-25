package com.leebeebeom.clothinghelper.ui.signin.ui

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import com.leebeebeom.clothinghelper.util.buildConfigLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Google 로그인 로직
 */
@HiltViewModel
abstract class BaseSignInGraphViewModel @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val firebaseAuthErrorUseCase: FirebaseAuthErrorUseCase,
) : ViewModel() {
    abstract val googleSignInState: GoogleSignInState

    fun signInWithGoogleEmail(activityResult: ActivityResult, showToast: ShowToast) =
        when (activityResult.resultCode) {
            Activity.RESULT_OK -> googleSignIn(
                activityResult = activityResult,
                showToast = showToast
            )
            Activity.RESULT_CANCELED -> {
                showToast(R.string.canceled)
                googleSignInState.googleButtonEnabled()
                null
            }
            else -> {
                buildConfigLog(
                    site = "signInWithGoogleEmail",
                    msg = "resultCode = ${activityResult.resultCode}",
                )
                showToast(R.string.unknown_error)
                googleSignInState.googleButtonEnabled()
                null
            }
        }

    private fun googleSignIn(activityResult: ActivityResult, showToast: ShowToast): Job {
        val handler = CoroutineExceptionHandler { _, throwable ->
            firebaseAuthErrorUseCase.firebaseAuthError(throwable = throwable, showToast = showToast)
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

abstract class GoogleSignInState {
    private var googleButtonEnabledState by mutableStateOf(true)
    private var isSignInLoadingState by mutableStateOf(false)

    fun googleButtonEnabled() {
        googleButtonEnabledState = true
    }

    fun googleButtonDisable() {
        googleButtonEnabledState = false
    }

    fun setLoading(loading: Boolean) {
        isSignInLoadingState = loading
    }

    private val googleButtonEnabledStream = snapshotFlow { googleButtonEnabledState }
    private val isSignInLoadingStream = snapshotFlow { isSignInLoadingState }
}