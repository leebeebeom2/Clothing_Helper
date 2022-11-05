package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.*
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val isLoading: StateFlow<Boolean>
    val isSignIn: StateFlow<Boolean>
    val user: StateFlow<User?>

    suspend fun googleSignIn(credential: Any?): AuthResult

    suspend fun signIn(signIn: SignIn): AuthResult

    suspend fun signUp(signUp: SignUp): AuthResult

    suspend fun resetPasswordEmail(email: String) : FirebaseResult

    suspend fun signOut()
}

typealias onDone = (FirebaseResult) -> Unit