package com.leebeebeom.clothinghelper.ui.signin

import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.OutlinedTextFieldAttr
import com.leebeebeom.clothinghelper.data.StateDelegator
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_USER_NOT_FOUND

object FirebaseErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
}

abstract class SignInBaseViewModel : ViewModel() {
    abstract val email: OutlinedTextFieldAttr

    abstract val isTextFieldEmpty: Boolean
    abstract val isErrorEnable: Boolean
    val firebaseButtonEnable get() = !isTextFieldEmpty && isErrorEnable

    private val _isProgressionOn = mutableStateOf(false)
    var isProgressionOn by StateDelegator(_isProgressionOn)
    private val _isFirebaseTaskSuccessful = mutableStateOf(false)
    var isFirebaseTaskSuccessful: Boolean
        get() {
            return if (_isFirebaseTaskSuccessful.value) {
                _isFirebaseTaskSuccessful.value = false
                true
            } else false
        }
        set(value) {
            _isFirebaseTaskSuccessful.value = value
        }

    val onFirebaseButtonClick = {
        isProgressionOn = true
        firebaseTask()
    }

    protected val onCompleteListener = { task: Task<*> ->
        if (task.isSuccessful) isFirebaseTaskSuccessful = true else taskFailed(task)
        isProgressionOn = false
    }

    private fun taskFailed(task: Task<*>) {
        (task.exception as? FirebaseAuthException)?.errorCode.let {
            when (it) {
                ERROR_INVALID_EMAIL -> email.errorEnable(R.string.error_invalid_email)
                ERROR_EMAIL_ALREADY_IN_USE -> email.errorEnable(R.string.error_email_already_in_use)
                ERROR_USER_NOT_FOUND -> email.errorEnable(R.string.error_user_not_found)
            }
        }
    }

    /**
     * FirebaseAuth.getInstance() 후 로직 작성
     * addOnComplete 인자에 firebaseAuthExecute 전달
     */
    protected abstract fun firebaseTask()
}

interface GoogleSignIn {
    var isFirebaseTaskSuccessful: Boolean
    var isProgressionOn: Boolean

    fun getGso(webClientId: String) =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

    fun signInWithCredentialExecute(intent: Intent?) {
        intent?.let {
            val account =
                GoogleSignIn.getSignedInAccountFromIntent(intent)
                    .getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) isFirebaseTaskSuccessful = true
                    else Log.d("TAG", "googleSignIn: 실패") // TODO 실패 로직
                }
        }
    }

    fun launch(
        googleSignInIntent: Intent,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        isProgressionOn = true
        launcher.launch(googleSignInIntent)
        isProgressionOn = false
    }
}