package com.leebeebeom.clothinghelper.data

import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTestUtil(repositoryProvider: RepositoryProvider) {
    val userRepository = repositoryProvider.createUserRepository()
    private val dispatcher = repositoryProvider.dispatcher

    suspend fun userCollect(
        backgroundScope: CoroutineScope,
        collect: suspend (User?) -> Unit = {},
    ) = backgroundScope.launch(dispatcher) {
        userRepository.user.collectLatest(action = collect)
    }

    suspend fun signIn(
        email: String = signInEmail,
        password: String = signInPassword,
    ) = userRepository.signIn(email = email, password = password)

    suspend fun signUp(
        email: String = signUpEmail,
        password: String = signInPassword,
        name: String = signUpName,
    ) = userRepository.signUp(email = email, password = password, name = name)

    fun signOut() = userRepository.signOut()

    fun deleteUser() = FirebaseAuth.getInstance().currentUser!!.delete()

    suspend fun assertSignIn(email: String = signInEmail) {
        val user = getUser()
        assert(user != null)
        assert(user?.email == email)
    }

    suspend fun assertSignOut() = assert(getUser() == null)

    suspend fun assertSignUp(
        email: String = signUpEmail,
        name: String = signUpName,
    ) {
        val user = getUser()
        assert(user != null)
        assert(user?.email == email)
        assert(user?.name == name)
    }

    suspend fun sendResetPasswordEmail(email: String = sendPasswordEmail) =
        userRepository.sendResetPasswordEmail(email = email)

    private suspend fun getUser() = userRepository.user.first()

    suspend fun getUid() = getUser()?.uid
}