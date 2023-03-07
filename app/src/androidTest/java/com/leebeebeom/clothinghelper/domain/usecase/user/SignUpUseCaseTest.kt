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
class SignUpUseCaseTest {
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private lateinit var signUpUseCase: SignUpUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        userRepositoryTestUtil =
            UserRepositoryTestUtil(repositoryProvider = RepositoryProvider(dispatcher = dispatcher))
        signUpUseCase = SignUpUseCase(userRepository = userRepositoryTestUtil.userRepository)
    }

    @Test
    fun signUpTest() = runTest(dispatcher) {
        userRepositoryTestUtil.signOut()

        userRepositoryTestUtil.userCollect(backgroundScope)
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut()


        signUpUseCase.signUp(
            email = invalidEmail,
            password = signInPassword,
            name = signUpName,
            firebaseResult = invalidEmailResult,
        )
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut()


        signUpUseCase.signUp(
            email = signInEmail,
            password = signInPassword,
            name = signUpName,
            firebaseResult = emailAlreadyInUserResult,
        )
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut()

        signUpUseCase.signUp(
            email = signUpEmail,
            password = signInPassword,
            name = signUpName,
            firebaseResult = successResult,
        )
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignUp()

        userRepositoryTestUtil.deleteUser()
    }
}