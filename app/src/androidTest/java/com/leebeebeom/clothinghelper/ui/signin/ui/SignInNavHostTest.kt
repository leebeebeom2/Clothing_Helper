package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.SIGN_IN_ROUTE
import com.leebeebeom.clothinghelper.ui.components.CENTER_DOT_PROGRESS_INDICATOR_TAG
import com.leebeebeom.clothinghelper.ui.components.showKeyboardOnceTest
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.RESET_PASSWORD_SCREEN_TAG
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SIGN_IN_SCREEN_TAG
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SIGN_UP_SCREEN_TAG
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInNavHostTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule = rule)
    private val device = UiDevice.getInstance(getInstrumentation())
    private lateinit var viewModel: SignInNavViewModel

    private val emailTextField get() = emailTextField(customTestRule)
    private val passwordTextField get() = passwordTextField(customTestRule)
    private val nickNameTextField get() = nickNameTextField(customTestRule)
    private val passwordConfirmTextField get() = passwordConfirmTextField(customTestRule)
    private val signUpButton get() = signUpButton(customTestRule)
    private val sendButton get() = sendButton(customTestRule)
    private val signInButton get() = signInButton(customTestRule)
    private val centerDotProgressIndicator get() = centerDotProgressIndicator(customTestRule)

    @Before
    fun init() {
        customTestRule.setContent {
            viewModel = hiltViewModel()
            NavHost(
                navController = rememberNavController(),
                startDestination = SIGN_IN_ROUTE,
            ) {
                composable(SIGN_IN_ROUTE) { SignInNavHost(viewModel = viewModel) }
            }
        }
    }

    @Test
    fun navigationTest() {
        customTestRule.signInScreen.exist()

        navigateToSignUp()
        device.pressBack() // popBackStack

        navigateToResetPassword()
        device.pressBack() // popBackStack

        customTestRule.signInScreen.exist()
    }

    @Test
    fun loadingTest() {
        fun inputEmail() = emailTextField.input(NOT_EXIST_EMAIL)
        fun inputEmailAndPassword() {
            inputEmail()
            passwordTextField.invisibleInput(PASSWORD)
        }

        fun loadingCheck() {
            centerDotProgressIndicator.exist(false)
            customTestRule.waitTagNotExist(CENTER_DOT_PROGRESS_INDICATOR_TAG, 5000)
        }

        inputEmailAndPassword()
        signInButton.click()
        loadingCheck()

        navigateToSignUp()
        inputEmailAndPassword()
        nickNameTextField.input(NICK_NAME)
        passwordConfirmTextField.invisibleInput(PASSWORD)
        signUpButton.click()
        loadingCheck()
        device.pressBack()

        navigateToResetPassword()
        inputEmail()
        sendButton.click()
        loadingCheck()
    }

    @Test
    fun blockBackPressTest() {
        fun blockBackPress() = customTestRule.restore(assert = ::doBlockBackPressTest)

        blockBackPress()

        navigateToSignUp()
        blockBackPress()
        customTestRule.waitTagNotExist(CENTER_DOT_PROGRESS_INDICATOR_TAG)
        device.pressBack()

        navigateToResetPassword()
        blockBackPress()
    }

    private fun doBlockBackPressTest() {
        val uiState = viewModel.uiState as MutableSignInNavUiState

        centerDotProgressIndicator.notExist()
        uiState.isSignInLoading = true
        centerDotProgressIndicator.exist()
        repeat(4) { device.pressBack() }
        centerDotProgressIndicator.exist()
        uiState.isSignInLoading = false
    }

    @Test
    fun showKeyboardOnceTestSignUp() =
        showKeyboardOnceTest(
            rule = customTestRule,
            focusNode = { emailTextField },
            firstScreenTag = SIGN_IN_SCREEN_TAG,
            secondScreenTag = SIGN_UP_SCREEN_TAG,
            move = {
                device.pressBack()
                navigateToSignUp()
            },
            moveBack = device::pressBack
        )

    @Test
    fun showKeyboardOnceTestResetPassword() =
        showKeyboardOnceTest(
            rule = customTestRule,
            focusNode = { emailTextField },
            firstScreenTag = SIGN_IN_SCREEN_TAG,
            secondScreenTag = RESET_PASSWORD_SCREEN_TAG,
            move = ::navigateToResetPassword,
            moveBack = device::pressBack
        )

    private fun navigateToSignUp() {
        customTestRule.getNodeWithStringRes(R.string.sign_up_with_email).click()
        customTestRule.getNodeWithTag(SIGN_UP_SCREEN_TAG).exist()

    }

    private fun navigateToResetPassword() {
        customTestRule.getNodeWithStringRes(R.string.forgot_password).click()
        customTestRule.getNodeWithTag(RESET_PASSWORD_SCREEN_TAG).exist()
    }

    private val CustomTestRule.signInScreen get() = getNodeWithTag(SIGN_IN_SCREEN_TAG)
}