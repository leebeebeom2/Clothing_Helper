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
class ResetPasswordUseCaseTest {
    private lateinit var resetPasswordUseCase: ResetPasswordUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        resetPasswordUseCase =
            ResetPasswordUseCase(userRepository = RepositoryProvider(dispatcher).createUserRepository())
    }

    @Test
    fun sendResetPasswordTest() = runTest(dispatcher) {
        resetPasswordUseCase.sendResetPasswordEmail( // invalid email
            email = invalidEmail,
            firebaseResult = invalidEmailResult
        )
        advanceUntilIdle()

        resetPasswordUseCase.sendResetPasswordEmail(
            email = notFoundEmail,
            firebaseResult = userNotFoundResult
        )
        advanceUntilIdle()

        resetPasswordUseCase.sendResetPasswordEmail( // 메일함 확인
            email = sendPasswordEmail,
            firebaseResult = successResult
        )
        advanceUntilIdle()
    }
}