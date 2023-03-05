package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.invalidEmailResult
import com.leebeebeom.clothinghelper.data.successResult
import com.leebeebeom.clothinghelper.data.userNotFoundResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResetPasswordUseCaseTest {
    lateinit var userRepository: UserRepository
    lateinit var resetPasswordUseCase: ResetPasswordUseCase
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)

    @Before
    fun init() {
        userRepository = repositoryProvider.getUserRepository()
        resetPasswordUseCase = ResetPasswordUseCase(userRepository)
    }

    @Test
    fun sendResetPasswordTest() = runTest(dispatcher) {
        resetPasswordUseCase.sendResetPasswordEmail(
            dispatcher,
            "invalidEmail",
            firebaseResult = invalidEmailResult
        )
        advanceUntilIdle()

        resetPasswordUseCase.sendResetPasswordEmail(
            dispatcher = dispatcher,
            email = "notexistEmail@a.com",
            firebaseResult = userNotFoundResult
        )
        advanceUntilIdle()

        resetPasswordUseCase.sendResetPasswordEmail( // 메일함 확인
            dispatcher = dispatcher,
            "boole92@naver.com",
            firebaseResult = successResult
        )
        advanceUntilIdle()
    }
}