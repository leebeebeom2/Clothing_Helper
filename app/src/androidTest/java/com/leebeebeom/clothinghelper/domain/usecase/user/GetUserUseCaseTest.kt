package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.*
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
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private lateinit var getUserUseCase: GetUserUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        FirebaseAuth.getInstance().signOut()

        userRepositoryTestUtil =
            UserRepositoryTestUtil(repositoryProvider = RepositoryProvider(dispatcher))
        getUserUseCase = GetUserUseCase(userRepository = userRepositoryTestUtil.userRepository)
    }

    @Test
    fun getUserTest() = runTest(dispatcher) {
        backgroundScope.launch(dispatcher) { getUserUseCase.user.collectLatest { } }
        advanceUntilIdle()
        assert(getUserUseCase.user.value == null)

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        assert(getUserUseCase.user.value != null)
        assert(getUserUseCase.user.value!!.email == signInEmail)

        userRepositoryTestUtil.signOut()
        advanceUntilIdle()
        assert(getUserUseCase.user.value == null)

        userRepositoryTestUtil.signUp()
        advanceUntilIdle()
        assert(getUserUseCase.user.value != null)
        assert(getUserUseCase.user.value!!.email == signUpEmail)
        assert(getUserUseCase.user.value!!.name == signUpName)

        userRepositoryTestUtil.deleteUser()
    }
}