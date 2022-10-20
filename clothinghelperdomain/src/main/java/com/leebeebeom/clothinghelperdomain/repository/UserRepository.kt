package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    fun isSignIn(): StateFlow<Boolean>
    fun getUser(): StateFlow<User>

    suspend fun googleSignIn(
        googleCredential: Any?, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ): MutableStateFlow<Boolean>

    suspend fun signIn(
        email: String,
        password: String,
        signInListener: FireBaseListeners.SignInListener
    )

    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FireBaseListeners.SignUpListener,
        updateNameListener: FireBaseListeners.UpdateNameListener
    )

    suspend fun resetPasswordEmail(
        email: String, resetPasswordListener: FireBaseListeners.ResetPasswordListener
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
        fun nameUpdateFailed()
    }

    interface ResetPasswordListener : FirebaseListener
    interface GoogleSignInListener : FirebaseListener {
        fun googleCredentialCastFailed()
    }
}