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
class GetSignInLoadingStateUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var getSignInLoadingStateUseCase: GetSignInLoadingStateUseCase
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)


    @Before
    fun init() {
        userRepository = repositoryProvider.getUserRepository()
        getSignInLoadingStateUseCase = GetSignInLoadingStateUseCase(userRepository = userRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadingFlowTest() = runTest(dispatcher) {
        backgroundScope.launch(dispatcher) { getSignInLoadingStateUseCase.isLoading.collectLatest {} }

        assert(!getSignInLoadingStateUseCase.isLoading.value)

        userRepository.signIn(
            email = "1@a.com", password = "111111",
            firebaseResult = successResult,
            dispatcher = dispatcher
        )
        assert(getSignInLoadingStateUseCase.isLoading.value)
        advanceUntilIdle()
        assert(!getSignInLoadingStateUseCase.isLoading.value)

        userRepository.signUp(
            email = "notExistEmail@a.com", password = "111111", name = "test",
            firebaseResult = successResult,
            dispatcher = dispatcher
        )
        assert(getSignInLoadingStateUseCase.isLoading.value)
        advanceUntilIdle()
        assert(!getSignInLoadingStateUseCase.isLoading.value)
        FirebaseAuth.getInstance().currentUser!!.delete()

        userRepository.sendResetPasswordEmail(
            email = "1@a.com", firebaseResult = successResult, dispatcher = dispatcher
        )
        assert(getSignInLoadingStateUseCase.isLoading.value)
        advanceUntilIdle()
        assert(!getSignInLoadingStateUseCase.isLoading.value)
    }

    private val successResult = object : FirebaseResult {
        override fun success() = assert(true)

        override fun fail(exception: Exception) = assert(false)

    }
}