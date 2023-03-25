package com.leebeebeom.clothinghelper.ui

import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.leebeebeom.clothinghelper.R

@OptIn(ExperimentalCoroutinesApi::class)
class ActivityViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val userRepository = UserRepositoryImpl(
        appScope = scope,
        dispatcher = dispatcher
    )
    private val getUserUseCase = GetUserUseCase(userRepository = userRepository)
    private val activityViewModel = ActivityViewModel(getUserUseCase)

    @Test
    fun activityUiStateTest() = runTest(dispatcher) {
        userRepository.signOut()
        waitTime()

        val uiState = activityViewModel.activityUiState
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { uiState.collect() }

        userRepository.signIn(email = SignInEmail, password = SignInPassword)
        waitTime()
        assert(uiState.value.user != null)
        assert(uiState.value.user!!.email == SignInEmail)

        userRepository.signOut()
        waitTime()
        assert(uiState.value.user == null)

        val toastText = R.string.sign_in_complete
        activityViewModel.addToastTextAtLast(toastText = toastText)
        assert(uiState.value.toastTexts.isNotEmpty())
        assert(uiState.value.toastTexts.first() == toastText)

        activityViewModel.removeFirstToastText()
        assert(uiState.value.user == null)
        assert(uiState.value.toastTexts.isEmpty())
    }
}