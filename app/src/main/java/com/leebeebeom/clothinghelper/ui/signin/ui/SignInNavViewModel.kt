package com.leebeebeom.clothinghelper.ui.signin.ui

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ShowToast
import com.leebeebeom.clothinghelper.data.repository.A_NETWORK_ERROR
import com.leebeebeom.clothinghelper.data.repository.util.TAG
import com.leebeebeom.clothinghelper.domain.model.AuthResult.*
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Google 로그인 로직
 */
@HiltViewModel
class SignInNavViewModel @Inject constructor(private val googleSignInUseCase: GoogleSignInUseCase) :
    ViewModel() {

    private val _signInNavUiState = MutableSignInNavUiState()
    val signInNavUiState: SignInNavUiState = _signInNavUiState


    fun signInWithGoogleEmail(activityResult: ActivityResult, showToast: ShowToast) {
        when (activityResult.resultCode) {
            Activity.RESULT_OK -> googleSignIn(
                activityResult = activityResult, showToast = showToast
            )
            Activity.RESULT_CANCELED -> {
                showToast(R.string.canceled)
                _signInNavUiState.googleButtonEnabled = true
            }
            // TODO 인터넷 미 연결 시 에러 메세지
            else -> {
                Log.e(TAG, "signInWithGoogleEmail: resultCode = ${activityResult.resultCode}")
                unknownFail(showToast)
            }
        }
    }

    private fun googleSignIn(activityResult: ActivityResult, showToast: ShowToast) {
        viewModelScope.launch {
            val result =
                googleSignInUseCase.googleSignIn(credential = getGoogleCredential(activityResult = activityResult),
                    onSubCategoriesLoadFail = { showToast(R.string.data_load_failed) })

            when (result) {
                is Success -> {
                    showToast(R.string.google_sign_in_complete)
                    _signInNavUiState.googleButtonEnabled = true
                }
                is Fail -> if (result.errorCode == A_NETWORK_ERROR) showToast(R.string.network_error)
                else unknownFail(showToast = showToast)
                is UnknownFail -> unknownFail(showToast = showToast)
            }
        }
    }

    private fun getGoogleCredential(activityResult: ActivityResult): AuthCredential {
        val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            .getResult(ApiException::class.java)
        return GoogleAuthProvider.getCredential(account.idToken, null)
    }

    private fun unknownFail(showToast: ShowToast) {
        showToast(R.string.unknown_error)
        _signInNavUiState.googleButtonEnabled = true
    }

    fun setGoogleButtonEnable(enable: Boolean) {
        _signInNavUiState.googleButtonEnabled = enable
    }
}

interface SignInNavUiState {
    val googleButtonEnabled: Boolean
}

class MutableSignInNavUiState : SignInNavUiState {
    override var googleButtonEnabled by mutableStateOf(true)
}

@Composable
fun getSignInNavViewModel(
    entry: NavBackStackEntry,
    navController: NavController
): SignInNavViewModel {
    val signInNavEntry =
        remember(entry) { navController.getBackStackEntry(SignInDestinations.SignInNav_Route) }
    return hiltViewModel(signInNavEntry)
}