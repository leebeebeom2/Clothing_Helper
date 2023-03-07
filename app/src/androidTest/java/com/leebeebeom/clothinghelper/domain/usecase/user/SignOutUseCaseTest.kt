package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.UserRepositoryTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignOutUseCaseTest {
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private lateinit var signOutUseCase: SignOutUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        userRepositoryTestUtil = UserRepositoryTestUtil(repositoryProvider = RepositoryProvider(dispatcher))
        signOutUseCase = SignOutUseCase(userRepository = userRepositoryTestUtil.userRepository)
    }

    @Test
    fun signOutTest() = runTest(dispatcher) {
        userRepositoryTestUtil.userCollect(backgroundScope)
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut() // not sign in

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignIn() // sign in

        signOutUseCase.signOut()
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut() // sign out
    }
}