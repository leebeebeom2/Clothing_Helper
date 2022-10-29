package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SignIn
import com.leebeebeom.clothinghelperdomain.model.SignUp
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
        signIn: SignIn,
        listener: FirebaseListener2,
    )

    fun signUp(
        signUp: SignUp,
        signUpListener: FirebaseListener2,
        updateNameListener: FirebaseListener2,
        pushInitialSubCategories: (uid: String) -> Unit
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