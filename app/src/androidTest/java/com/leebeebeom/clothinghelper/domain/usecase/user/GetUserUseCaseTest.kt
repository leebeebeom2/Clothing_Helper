package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetUserUseCaseTest {
    lateinit var userRepository: UserRepository
    lateinit var getUserUseCase: GetUserUseCase
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)

    @Before
    fun init() {
        FirebaseAuth.getInstance().signOut()
        userRepository = repositoryProvider.getUserRepository()
        getUserUseCase = GetUserUseCase(userRepository)
    }

    @Test
    fun getUserTest() = runTest(dispatcher) {
        backgroundScope.launch(dispatcher) { getUserUseCase.user.collectLatest {} }

        assert(getUserUseCase.user.value == null)

        userRepository.signIn(
            email = "1@a.com",
            password = "111111",
            firebaseResult = successResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(getUserUseCase.user.value != null)
        assert(getUserUseCase.user.value!!.email == "1@a.com")

        userRepository.signOut()
        advanceUntilIdle()
        assert(getUserUseCase.user.value == null)

        userRepository.signUp(
            email = "2@a.com",
            password = "111111",
            name = "test",
            firebaseResult = successResult,
            dispatcher = dispatcher
        )
        advanceUntilIdle()
        assert(getUserUseCase.user.value != null)
        assert(getUserUseCase.user.value!!.email == "2@a.com")
        assert(getUserUseCase.user.value!!.name == "test")

        FirebaseAuth.getInstance().currentUser!!.delete()
    }

    private val successResult = object : FirebaseResult {
        override fun success() = assert(true)

        override fun fail(exception: Exception) = assert(false)
    }
}