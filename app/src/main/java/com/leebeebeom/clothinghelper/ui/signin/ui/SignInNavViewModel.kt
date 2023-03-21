package com.leebeebeom.clothinghelper.ui.signin.ui

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GetSignInLoadingStreamUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import com.leebeebeom.clothinghelper.util.buildConfigLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Google 로그인 로직
 */
@HiltViewModel
class SignInNavViewModel @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase,
    getSignInLoadingStreamUseCase: GetSignInLoadingStreamUseCase,
    private val firebaseAuthErrorUseCase: FirebaseAuthErrorUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var googleButtonEnabled by mutableStateOf(true)

    val uiState = combine(
        flow = snapshotFlow { googleButtonEnabled },
        flow2 = getSignInLoadingStreamUseCase.signInLoadingStream
    ) { googleButtonEnabled, isLoading ->
        SignInNavUiState(googleButtonEnabled = googleButtonEnabled, isLoading = isLoading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignInNavUiState()
    )

    fun signInWithGoogleEmail(activityResult: ActivityResult, showToast: ShowToast) {
        when (activityResult.resultCode) {
            Activity.RESULT_OK -> googleSignIn(
                activityResult = activityResult, showToast = showToast
            )
            Activity.RESULT_CANCELED -> {
                showToast(R.string.canceled)
                savedStateHandle[GoogleButtonEnabledKey] = true
            }
            else -> {
                buildConfigLog(
                    site = "signInWithGoogleEmail",
                    msg = "resultCode = ${activityResult.resultCode}",
                )
                showToast(R.string.unknown_error)
                savedStateHandle[GoogleButtonEnabledKey] = true
            }
        }
    }

    private fun googleSignIn(activityResult: ActivityResult, showToast: ShowToast) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            firebaseAuthErrorUseCase.firebaseAuthError(throwable = throwable, showToast = showToast)
            googleButtonEnabled = true
        }

        viewModelScope.launch(handler) {
            googleSignInUseCase.googleSignIn(credential = getGoogleCredential(activityResult = activityResult))
            showToast(R.string.google_sign_in_complete)
        }
    }

    private fun getGoogleCredential(activityResult: ActivityResult): AuthCredential {
        val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            .getResult(ApiException::class.java)
        return GoogleAuthProvider.getCredential(account.idToken, null)
    }

    fun setGoogleButtonEnable(enable: Boolean) {
        googleButtonEnabled = enable
    }
}

data class SignInNavUiState(
    val googleButtonEnabled: Boolean = true,
    val isLoading: Boolean = false,
)