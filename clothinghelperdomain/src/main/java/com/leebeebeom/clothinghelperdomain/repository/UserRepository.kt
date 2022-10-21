package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    fun isSignIn(): StateFlow<Boolean>
    fun getUser(): StateFlow<User?>

    fun googleSignIn(
        googleCredential: Any?, googleSignInListener: FirebaseListener
    )

    fun signIn(
        email: String, password: String, signInListener: FirebaseListener
    )

    fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FirebaseListener,
        updateNameListener: FirebaseListener
    )

    fun resetPasswordEmail(
        email: String, resetPasswordListener: FirebaseListener
    )

    fun signOut()
}

interface FirebaseListener {
    fun taskSuccess()
    fun taskFailed(exception: Exception?)
}