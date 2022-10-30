package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SignIn
import com.leebeebeom.clothinghelperdomain.model.SignUp
import com.leebeebeom.clothinghelperdomain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val isLoading: StateFlow<Boolean>
    val isSignIn: StateFlow<Boolean>
    val user: StateFlow<User?>

    fun googleSignIn(
        credential: Any?,
        onDone: onDone,
        pushInitialSubCategories: (uid: String) -> Unit
    )

    fun signIn(
        signIn: SignIn,
        onDone: onDone
    )

    fun signUp(
        signUp: SignUp,
        onSignUpDone: onDone,
        onNameUpdateDone: onDone,
        pushInitialSubCategories: (uid: String) -> Unit
    )

    fun resetPasswordEmail(
        email: String,
        onDone: onDone
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

typealias onDone = (FirebaseResult) -> Unit