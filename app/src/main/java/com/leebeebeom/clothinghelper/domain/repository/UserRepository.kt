package com.leebeebeom.clothinghelper.domain.repository

import com.google.firebase.auth.AuthCredential
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.data.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository : LoadingStateProvider {
    val isSignIn: StateFlow<Boolean>
    val user: StateFlow<User?>

    suspend fun googleSignIn(
        credential: AuthCredential,
        firebaseResult: FirebaseResult,
        allLocalDataClear: suspend () -> Unit,
    )

    suspend fun signIn(
        email: String,
        password: String,
        firebaseResult: FirebaseResult,
        allLocalDataClear: suspend () -> Unit,
    )

    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        firebaseResult: FirebaseResult,
        allLocalDataClear: suspend () -> Unit,
    )

    suspend fun resetPasswordEmail(email: String, firebaseResult: FirebaseResult)
    suspend fun signOut(onFail: (Exception) -> Unit, allLocalDataClear: suspend () -> Unit)
}