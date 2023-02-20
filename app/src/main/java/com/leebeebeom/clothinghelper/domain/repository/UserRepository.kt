package com.leebeebeom.clothinghelper.domain.repository

import com.google.firebase.auth.AuthCredential
import com.leebeebeom.clothinghelper.domain.model.AuthResult
import com.leebeebeom.clothinghelper.domain.model.data.User
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import kotlinx.coroutines.flow.StateFlow

interface UserRepository : LoadingStateProvider {
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