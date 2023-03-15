package com.leebeebeom.clothinghelper.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

const val userLoadingTag = "loading"

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTestUtil(repositoryProvider: RepositoryProvider) {
    val userRepository = repositoryProvider.createUserRepository()
    private val dispatcher = repositoryProvider.dispatcher

    suspend fun userCollect(
        backgroundScope: CoroutineScope,
        collect: suspend (Result<User?>) -> Unit = {},
    ) = backgroundScope.launch(dispatcher) {
        userRepository.user.collectLatest(action = collect)
    }

    suspend fun signIn(
        email: String = SignInEmail,
        password: String = SignInPassword,
    ) = userRepository.signIn(email = email, password = password)

    suspend fun signUp(
        email: String = SignUpEmail,
        password: String = SignInPassword,
        name: String = SignUpName,
    ) = userRepository.signUp(email = email, password = password, name = name)

    fun signOut() = userRepository.signOut()

    suspend fun deleteUser() {
        FirebaseAuth.getInstance().currentUser!!.delete().await()
    }

    suspend fun sendResetPasswordEmail(email: String = SendPasswordEmail) =
        userRepository.sendResetPasswordEmail(email = email)

    suspend fun getUser() = userRepository.user.first().getOrThrow()

    suspend fun getUid() = getUser()?.uid

    fun loadingCollect(backgroundScope: CoroutineScope) =
        backgroundScope.launch(dispatcher) {
            userRepository.isLoading.collect {
                Log.d(userLoadingTag, "userRepository loading collect 호출: $it")
            }
        }
}