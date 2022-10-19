package com.leebeebeom.domain.repository

import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes

interface UserRepository {
    fun signIn(email: String, password: String, signInListener: FireBaseListeners.SignInListener)
    fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FireBaseListeners.SignUpListener,
        updateNameListener: FireBaseListeners.UpdateNameListener
    )

    fun googleSignIn(
        activityResult: ActivityResult, googleSignInListener: FireBaseListeners.GoogleSignInListener
    )

    fun resetPasswordEmail(
        email: String,
        resetPasswordListener: FireBaseListeners.ResetPasswordListener
    )
}

interface FirebaseListener {
    fun taskStart()
    fun taskSuccess()
    fun taskFailed(exception: Exception?)
    fun taskFinish()
}

sealed class FireBaseListeners {
    interface SignUpListener : FirebaseListener
    interface SignInListener : FirebaseListener
    interface UpdateNameListener : FirebaseListener {
        fun userNull()
        fun taskFailed()
    }

    interface ResetPasswordListener : FirebaseListener
    interface GoogleSignInListener : FirebaseListener {
        fun googleSignInFailed(@StringRes toastText: Int)
    }
}
