package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.leebeebeom.clothinghelper.CustomTestRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.activityRule
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
        customTestRule.getNodeWithTag(SIGN_IN_NAV_TAG).exist()
        navigateToSignUp()
        customTestRule.getNodeWithTag(SIGN_UP_SCREEN_TAG).exist()
        device.pressBack()
        navigateToResetPassword()
        customTestRule.getNodeWithTag(RESET_PASSWORD_SCREEN_TAG).exist()
        device.pressBack()
        navigateToResetPassword()
        customTestRule.getNodeWithTag(SIGN_IN_NAV_TAG).exist()
    }

    @Test
    fun loadingTest() {
        fun inputEmail() = customTestRule.emailTextField.input("test@a.com")
        fun inputEmailAndPassword() {
            inputEmail()
            customTestRule.passwordTextField.invisibleInput("testPassWord")
        }

        fun loadingCheck() {
            customTestRule.centerDotProgressIndicator.exist(false)
            customTestRule.waitTagNotExist(CENTER_DOT_PROGRESS_INDICATOR_TAG, 5000)
        }

        inputEmailAndPassword()
        customTestRule.signInButton.click()
        loadingCheck()

        navigateToSignUp()
        customTestRule.waitTagExist(SIGN_UP_SCREEN_TAG)
        inputEmailAndPassword()
        customTestRule.getNodeWithText(rule.activity.getString(R.string.nickname)).input("닉네임")
        customTestRule.getNodeWithText(rule.activity.getString(R.string.password_confirm))
            .invisibleInput("testPassWord")
        customTestRule.getNodeWithText(rule.activity.getString(R.string.sign_up)).click()
        loadingCheck()

        device.pressBack()
        navigateToResetPassword()
        inputEmail()
        customTestRule.getNodeWithText(rule.activity.getString(R.string.send)).click()
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

        customTestRule.centerDotProgressIndicator.notExist()
        uiState.isSignInLoading = true
        customTestRule.centerDotProgressIndicator.exist()
        repeat(4) { device.pressBack() }
        customTestRule.centerDotProgressIndicator.exist()
        uiState.isSignInLoading = false
    }

    @Test
    fun showKeyboardOnceTestSignUp() =
        showKeyboardOnceTest(
            rule = customTestRule,
            focusNode = { customTestRule.emailTextField },
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
            focusNode = { customTestRule.emailTextField },
            firstScreenTag = SIGN_IN_SCREEN_TAG,
            secondScreenTag = RESET_PASSWORD_SCREEN_TAG,
            move = ::navigateToResetPassword,
            moveBack = device::pressBack
        )

    private fun navigateToSignUp() =
        customTestRule.getNodeWithText(rule.activity.getString(R.string.sign_up_with_email)).click()

    private fun navigateToResetPassword() =
        customTestRule.getNodeWithText(rule.activity.getString(R.string.forgot_password)).click()
}