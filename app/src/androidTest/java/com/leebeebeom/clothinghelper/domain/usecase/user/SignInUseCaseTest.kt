package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInUseCaseTest {
    private lateinit var signInUseCase: SignInUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        signInUseCase =
            SignInUseCase(userRepository = RepositoryProvider(dispatcher).createUserRepository())
    }

    @Test
    fun signInTest() = runTest(dispatcher) {
        signInUseCase.signIn(
            email = invalidEmail,
            password = signInPassword,
            firebaseResult = invalidEmailResult
        )
        advanceUntilIdle()

        signInUseCase.signIn(
            email = notFoundEmail,
            password = signInPassword,
            firebaseResult = userNotFoundResult
        )
        advanceUntilIdle()

        signInUseCase.signIn(
            email = signInEmail,
            password = wrongPassword,
            firebaseResult = wrongPasswordResult
        )
        advanceUntilIdle()

        signInUseCase.signIn(
            email = signInEmail,
            password = signInPassword,
            firebaseResult = successResult
        )
    }
}