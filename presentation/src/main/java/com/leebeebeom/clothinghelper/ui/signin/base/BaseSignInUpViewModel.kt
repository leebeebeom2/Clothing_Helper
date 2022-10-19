package com.leebeebeom.clothinghelper.ui.signin.base

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.repository.FireBaseListeners
import com.leebeebeom.clothinghelper.domain.usecase.subcategory.WriteInitialSubCategoryUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.ui.TAG

abstract class BaseSignInUpViewModel(
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val writeInitialSubCategoryUseCase: WriteInitialSubCategoryUseCase
) : ViewModel() {
    abstract val viewModelState: BaseSignInUpViewModelState

    fun showToast(@StringRes toastText: Int) = viewModelState.showToast(toastText)
    fun loadingOn() = viewModelState.loadingOn()
    fun loadingOff() = viewModelState.loadingOff()
    fun googleButtonDisable() = viewModelState.googleButtonDisable()
    fun googleButtonEnable() = viewModelState.googleButtonEnable()

    fun signInWithGoogleEmail(activityResult: ActivityResult) =
        googleSignInUseCase(activityResult, googleSignInListener)

    private val googleSignInListener = object : FireBaseListeners.GoogleSignInListener {
        override fun googleSignInFailed(activityResult: ActivityResult) {
            if (activityResult.data == null) {
                showToast(R.string.unknown_error)
                Log.d(TAG, "googleSignInFailed: activityResult.data = null")
            } else
                if (activityResult.resultCode == Activity.RESULT_CANCELED) showToast(R.string.canceled)
                else {
                    showToast(R.string.unknown_error)
                    Log.d(TAG, "googleSignInFailed: $activityResult")
                }
        }

        override fun taskStart() {
            loadingOn()
            googleButtonDisable()
        }

        override fun taskSuccess(authResult: AuthResult?) {
            authResult?.additionalUserInfo?.run {
                if (isNewUser) writeInitialSubCategoryUseCase()
            }
            showToast(R.string.google_login_complete)
        }

        override fun taskFailed(exception: Exception?) {
            showToast(R.string.unknown_error)
            Log.d(TAG, "taskFailed: $exception")
        }

        override fun taskFinish() {
            loadingOff()
            googleButtonEnable()
        }

    }
}

object FirebaseErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
}