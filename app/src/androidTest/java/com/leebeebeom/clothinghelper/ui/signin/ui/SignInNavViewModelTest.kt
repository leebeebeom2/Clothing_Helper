package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.lifecycle.SavedStateHandle
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.UserRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.wait
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GetSignInLoadingStateUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInNavViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)
    private val userRepositoryTestUtil = UserRepositoryTestUtil(repositoryProvider)
    private lateinit var signInNavViewModel: SignInNavViewModel

    @Before
    fun init() {
        val googleSignInUseCase =
            GoogleSignInUseCase(userRepository = userRepositoryTestUtil.userRepository)
        val getSignInLoadingStateUseCase =
            GetSignInLoadingStateUseCase(userRepository = userRepositoryTestUtil.userRepository)
        signInNavViewModel = SignInNavViewModel(
            googleSignInUseCase = googleSignInUseCase,
            getSignInLoadingStateUseCase = getSignInLoadingStateUseCase,
            firebaseAuthErrorUseCase = FirebaseAuthErrorUseCase(),
            savedStateHandle = SavedStateHandle(mapOf(GoogleButtonEnabledKey to true))
        )
    }

    @Test
    fun signInNavUiStateTest() = runTest(dispatcher) {
        val uiState = signInNavViewModel.uiState
        backgroundScope.launch(dispatcher) { uiState.collectLatest {} }

        signInNavViewModel.setGoogleButtonEnable(false)
        wait()
        assert(!uiState.value.googleButtonEnabled)

        signInNavViewModel.setGoogleButtonEnable(true)
        wait()
        assert(uiState.value.googleButtonEnabled)
    }
}