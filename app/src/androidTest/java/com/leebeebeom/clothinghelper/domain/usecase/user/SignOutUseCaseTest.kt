package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.successResult
import com.leebeebeom.clothinghelper.data.userCollect
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignOutUseCaseTest {
    lateinit var userRepository: UserRepository
    lateinit var signOutUseCase: SignOutUseCase
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)

    @Before
    fun init() {
        userRepository = repositoryProvider.getUserRepository()
        signOutUseCase = SignOutUseCase(userRepository)
    }

    @Test
    fun signOutTest() = runTest(dispatcher) {
        userCollect(dispatcher = dispatcher, userRepository = userRepository)

        assert(userRepository.user.value == null)

        userRepository.signIn(
            email = "1@a.com",
            password = "111111",
            dispatcher = dispatcher,
            firebaseResult = successResult
        )
        advanceUntilIdle()
        assert(userRepository.user.value != null)
        assert(userRepository.user.value!!.email == "1@a.com")

        userRepository.signOut()
        advanceUntilIdle()
        assert(userRepository.user.value == null)
    }
}