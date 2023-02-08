package com.leebeebeom.clothinghelper.domain.repository

import com.google.firebase.auth.AuthCredential
import com.leebeebeom.clothinghelper.domain.model.AuthResult
import com.leebeebeom.clothinghelper.domain.model.data.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository : LoadingState {
    val isSignIn: StateFlow<Boolean>
    val user: StateFlow<User?>

    suspend fun googleSignIn(credential: AuthCredential): AuthResult
    suspend fun signIn(
        email: String,
        password: String,
    ): AuthResult

    suspend fun signUp(
        email: String,
        password: String,
        name: String,
    ): AuthResult

    suspend fun resetPasswordEmail(email: String): AuthResult
    suspend fun signOut(): AuthResult
}