package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInNavViewModelTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()

    private val dispatcher = StandardTestDispatcher()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val userRepository: UserRepository =
        UserRepositoryImpl(appScope = scope, dispatcher = dispatcher)
    private val signInNavViewModel =
        SignInNavViewModel(
            googleSignInUseCase = GoogleSignInUseCase(userRepository),
            firebaseAuthErrorUseCase = FirebaseAuthErrorUseCase()
        )
    private val uiState = signInNavViewModel.uiState

    @Before
    fun init() {
        rule.setContent {
            uiState.collectAsStateWithLifecycle()
        }
    }

    @Test
    fun signInNavUiStateTest() = runTest(dispatcher) {
        val state = signInNavViewModel.signInNavState

        state.googleButtonDisable()
        waitTime()
        assert(!uiState.value.googleButtonEnabled)

        state.googleButtonEnabled()
        waitTime()
        assert(uiState.value.googleButtonEnabled)

        state.setLoading(true)
        waitTime()
        assert(uiState.value.isLoading)

        state.setLoading(false)
        waitTime()
        assert(!uiState.value.isLoading)
    }
}