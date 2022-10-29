package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val isSignIn: StateFlow<Boolean>
    val user: StateFlow<User?>

    fun googleSignIn(
        credential: Any?,
        listener: FirebaseListener2,
        pushInitialSubCategories: (uid: String) -> Unit
    )

    fun signIn(
        email: String,
        password: String,
        signInListener: FirebaseListener,
        taskFinish: () -> Unit
    )

    fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FirebaseListener,
        updateNameListener: FirebaseListener,
        pushInitialSubCategories: (uid: String) -> Unit,
        taskFinish: () -> Unit
    )

    fun resetPasswordEmail(
        email: String,
        resetPasswordListener: FirebaseListener,
        taskFinish: () -> Unit
    )

    fun signOut()
}

interface FirebaseListener {
    fun taskSuccess()
    fun taskFailed(exception: Exception?)
}

interface FirebaseListener2 {
    fun taskSuccess()
    fun taskFailed(exception: Exception?)
    fun taskFinish()
}