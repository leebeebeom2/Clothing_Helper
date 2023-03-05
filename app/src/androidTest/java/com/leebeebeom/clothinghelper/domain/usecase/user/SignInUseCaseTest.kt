package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.invalidEmailResult
import com.leebeebeom.clothinghelper.data.successResult
import com.leebeebeom.clothinghelper.data.userNotFoundResult
import com.leebeebeom.clothinghelper.data.wrongPasswordResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInUseCaseTest {
    lateinit var userRepository: UserRepository
    lateinit var signInUseCase: SignInUseCase
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)

    @Before
    fun init() {
        userRepository = repositoryProvider.getUserRepository()
        signInUseCase = SignInUseCase(userRepository)
    }

    @Test
    fun signInTest() = runTest(dispatcher) {
        signInUseCase.signIn(
            dispatcher = dispatcher,
            email = "invalidEmail",
            password = "111111",
            firebaseResult = invalidEmailResult
        )
        advanceUntilIdle()

        signInUseCase.signIn(
            dispatcher = dispatcher,
            email = "notexistemail@a.com",
            password = "111111",
            firebaseResult = userNotFoundResult
        )
        advanceUntilIdle()

        signInUseCase.signIn(
            dispatcher = dispatcher,
            email = "1@a.com",
            password = "123456",
            firebaseResult = wrongPasswordResult
        )
        advanceUntilIdle()

        signInUseCase.signIn(
            dispatcher = dispatcher,
            email = "1@a.com",
            password = "111111",
            firebaseResult = successResult
        )
    }
}