package com.leebeebeom.clothinghelper.domain.repository

import com.google.firebase.auth.AuthCredential
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import com.leebeebeom.clothinghelper.domain.model.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface UserRepository : LoadingStateProvider {
    val firebaseUser: Flow<FirebaseUser?>

    suspend fun googleSignIn(
        credential: AuthCredential,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    )

    suspend fun signIn(
        email: String,
        password: String,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    )

    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    )

    suspend fun resetPasswordEmail(
        email: String, firebaseResult: FirebaseResult,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    )

    suspend fun signOut(
        onFail: (Exception) -> Unit,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    )
}