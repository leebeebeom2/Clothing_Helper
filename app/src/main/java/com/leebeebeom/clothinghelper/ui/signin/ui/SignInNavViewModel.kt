package com.leebeebeom.clothinghelper.ui.signin.ui

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.repository.util.TAG
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GetSignInLoadingStateUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val GoogleButtonEnabledKey = "google button enable"

/**
 * Google 로그인 로직
 */
@HiltViewModel
class SignInNavViewModel @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase,
    getSignInLoadingStateUseCase: GetSignInLoadingStateUseCase,
    private val firebaseAuthErrorUseCase: FirebaseAuthErrorUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val googleButtonEnabled =
        savedStateHandle.getStateFlow(key = GoogleButtonEnabledKey, initialValue = true)

    val uiState = combine(
        flow = googleButtonEnabled, flow2 = getSignInLoadingStateUseCase.isLoading
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
                activityResult = activityResult,
                showToast = showToast
            )
            Activity.RESULT_CANCELED -> {
                showToast(R.string.canceled)
                savedStateHandle[GoogleButtonEnabledKey] = true
            }
            else -> {
                Log.e(TAG, "signInWithGoogleEmail: resultCode = ${activityResult.resultCode}")
                showToast(R.string.unknown_error)
                savedStateHandle[GoogleButtonEnabledKey] = true
            }
        }
    }

    private fun googleSignIn(activityResult: ActivityResult, showToast: ShowToast) {
        runCatching {
            viewModelScope.launch {
                googleSignInUseCase.googleSignIn(
                    credential = getGoogleCredential(
                        activityResult = activityResult
                    )
                )
            }
        }.onSuccess {
            showToast(R.string.google_sign_in_complete)
        }.onFailure {
            firebaseAuthErrorUseCase.firebaseAuthError(
                exception = it,
                showToast = showToast
            )
            savedStateHandle[GoogleButtonEnabledKey] = true
        }
    }

    private fun getGoogleCredential(activityResult: ActivityResult): AuthCredential {
        val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            .getResult(ApiException::class.java)
        return GoogleAuthProvider.getCredential(account.idToken, null)
    }

    fun setGoogleButtonEnable(enable: Boolean) {
        savedStateHandle[GoogleButtonEnabledKey] = enable
    }
}

data class SignInNavUiState(
    val googleButtonEnabled: Boolean = true,
    val isLoading: Boolean = false,
)