package com.leebeebeom.clothinghelper.ui

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.UserRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.signInEmail
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActivityViewModelTest {
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private lateinit var activityViewModel: ActivityViewModel
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        userRepositoryTestUtil = UserRepositoryTestUtil(repositoryProvider)
        val getUserUseCase = GetUserUseCase(userRepositoryTestUtil.userRepository)
        activityViewModel = ActivityViewModel(getUserUseCase)
    }

    @Test
    fun activityUiStateTest() = runTest(dispatcher) {
        val uiState = activityViewModel.activityUiState
        backgroundScope.launch(dispatcher) {
            uiState.collectLatest { }
        }

        assert(uiState.value.user == null)
        assert(uiState.value.toastText == null)

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        assert(uiState.value.user != null)
        assert(uiState.value.user!!.email == signInEmail)
        assert(uiState.value.toastText == null)

        userRepositoryTestUtil.signOut()
        advanceUntilIdle()
        withContext(Dispatchers.Default) {
            delay(1000)
            assert(uiState.value.user == null)
            assert(uiState.value.toastText == null)
        }

        val toastText = R.string.sign_in_complete
        activityViewModel.showToast(toastText = toastText)
        advanceUntilIdle()
        withContext(Dispatchers.Default) {
            delay(1000)
            assert(uiState.value.user == null)
            assert(uiState.value.toastText != null)
            assert(uiState.value.toastText == toastText)
        }

        activityViewModel.toastShown()
        advanceUntilIdle()
        withContext(Dispatchers.Default) {
            delay(1000)
            assert(uiState.value.user == null)
            assert(uiState.value.toastText == null)
        }
    }
}