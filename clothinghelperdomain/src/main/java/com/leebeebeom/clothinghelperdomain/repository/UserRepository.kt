package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository : LoadingRepository {
    val isSignIn: StateFlow<Boolean>
    val user: StateFlow<User?>

    suspend fun googleSignIn(credential: Any?): AuthResult
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signUp(email: String, password: String, name: String): AuthResult
    suspend fun resetPasswordEmail(email: String): AuthResult
    suspend fun signOut(): AuthResult
}