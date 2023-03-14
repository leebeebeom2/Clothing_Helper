package com.leebeebeom.clothinghelper.ui

import androidx.lifecycle.SavedStateHandle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.UserRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.signInEmail
import com.leebeebeom.clothinghelper.data.wait
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
        activityViewModel = ActivityViewModel(
            getUserUseCase = getUserUseCase,
            savedStateHandle = SavedStateHandle(mapOf(toastTextKey to emptyList<Int>()))
        )
    }

    @Test
    fun activityUiStateTest() = runTest(dispatcher) {
        val uiState = activityViewModel.activityUiState
        backgroundScope.launch(dispatcher) { uiState.collectLatest { } }

        assert(uiState.value.user == null)

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        assert(uiState.value.user != null)
        assert(uiState.value.user!!.email == signInEmail)

        userRepositoryTestUtil.signOut()
        advanceUntilIdle()
        wait()
        assert(uiState.value.user == null)

        val toastText = R.string.sign_in_complete
        activityViewModel.showToast(toastText = toastText)
        advanceUntilIdle()
        wait()
        assert(uiState.value.user == null)
        assert(uiState.value.toastText.isNotEmpty())
        assert(uiState.value.toastText.first() == toastText)

        activityViewModel.toastShown()
        advanceUntilIdle()
        wait()
        assert(uiState.value.user == null)
        assert(uiState.value.toastText.isEmpty())
    }
}