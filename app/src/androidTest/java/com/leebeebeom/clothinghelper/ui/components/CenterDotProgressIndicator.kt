package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignInUseCase
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreenTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreenTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInViewModel
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.waitTagExist
import com.leebeebeom.clothinghelper.ui.waitTagNotExist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CenterDotProgressIndicatorTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val userRepository = UserRepositoryImpl(
        CoroutineScope(SupervisorJob() + Dispatchers.Default),
        StandardTestDispatcher()
    )
    private val viewModel = SignInViewModel(
        googleSignInUseCase = GoogleSignInUseCase(userRepository),
        signInUseCase = SignInUseCase(userRepository),
        firebaseAuthErrorUseCase = FirebaseAuthErrorUseCase(),
        savedStateHandle = SavedStateHandle()
    )

    @Before
    fun init() {
        rule.setContent {
            ClothingHelperTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "resetPassword") {
                    composable("resetPassword") {
                        ResetPasswordScreen(popBackStack = { }, showToast = {})
                    }
                    composable("signIn") {
                        SignInScreen(
                            navigateToResetPassword = { },
                            navigateToSignUp = { },
                            showToast = {},
                            viewModel = viewModel
                        )
                    }
                }
                navController.navigate("signIn")
            }
        }
    }

    @Test
    fun blockBackPressTest() {
        rule.waitTagExist(SignInScreenTag)

        val state = viewModel.signInState
        state.setLoading(true)

        rule.waitTagExist(CenterDotProgressIndicatorTag)
        repeat(5) { device.pressBack() }

        state.setLoading(false)
        rule.waitTagNotExist(CenterDotProgressIndicatorTag)
        device.pressBack()
        rule.waitTagExist(ResetPasswordScreenTag)
    }
}