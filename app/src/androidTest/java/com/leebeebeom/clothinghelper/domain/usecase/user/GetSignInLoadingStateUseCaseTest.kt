package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.UserRepositoryTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetSignInLoadingStateUseCaseTest {
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private lateinit var getSignInLoadingStateUseCase: GetSignInLoadingStateUseCase
    private val dispatcher = StandardTestDispatcher()


    @Before
    fun init() {
        userRepositoryTestUtil = UserRepositoryTestUtil(RepositoryProvider(dispatcher))
        getSignInLoadingStateUseCase =
            GetSignInLoadingStateUseCase(userRepository = userRepositoryTestUtil.userRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadingFlowTest() = runTest(dispatcher) {
        backgroundScope.launch(dispatcher) { getSignInLoadingStateUseCase.isLoading.collectLatest {} }

        assert(!getSignInLoadingStateUseCase.isLoading.value) // not loading

        userRepositoryTestUtil.signIn()
        assert(getSignInLoadingStateUseCase.isLoading.value)
        advanceUntilIdle()
        assert(!getSignInLoadingStateUseCase.isLoading.value)

        userRepositoryTestUtil.signUp()
        assert(getSignInLoadingStateUseCase.isLoading.value)
        advanceUntilIdle()
        assert(!getSignInLoadingStateUseCase.isLoading.value)
        userRepositoryTestUtil.deleteUser()

        userRepositoryTestUtil.sendResetPasswordEmail()
        assert(getSignInLoadingStateUseCase.isLoading.value)
        advanceUntilIdle()
        assert(!getSignInLoadingStateUseCase.isLoading.value)
    }
}