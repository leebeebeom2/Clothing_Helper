package com.leebeebeom.clothinghelperdomain.repository

import androidx.activity.result.ActivityResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val isLogin: StateFlow<Boolean>
    val name: StateFlow<String>
    val email: StateFlow<String>
    val uid: StateFlow<String>

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

    fun nameUpdate(name: String)
}

interface FirebaseListener {
    fun taskStart()
    fun taskSuccess(authResult: AuthResult?)
    fun taskFailed(exception: Exception?)
    fun taskFinish()
}

sealed class FireBaseListeners {
    interface SignUpListener : FirebaseListener
    interface SignInListener : FirebaseListener
    interface UpdateNameListener : FirebaseListener {
        fun userNull()
        fun nameUpdateFailed()
    }

    interface ResetPasswordListener : FirebaseListener
    interface GoogleSignInListener : FirebaseListener {
        fun googleSignInFailed(activityResult: ActivityResult)
    }
}