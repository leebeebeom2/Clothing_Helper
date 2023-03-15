package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.data.*
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.SignInGraphRoute
import com.leebeebeom.clothinghelper.ui.MainActivityScreen
import com.leebeebeom.clothinghelper.ui.components.CENTER_DOT_PROGRESS_INDICATOR_TAG
import com.leebeebeom.clothinghelper.ui.components.showKeyboardOnceTest
import com.leebeebeom.clothinghelper.ui.main.MainNavTag
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
    private val signUpButton get() = signUpButton(customTestRule)
    private val sendButton get() = sendButton(customTestRule)
    private val signInButton get() = signInButton(customTestRule)
    private val centerDotProgressIndicator get() = centerDotProgressIndicator(customTestRule)

    @Before
    fun init() {
        Firebase.auth.signOut()

        customTestRule.setContent {
            viewModel = hiltViewModel()

            NavHost(
                navController = rememberNavController(),
                startDestination = SignInGraphRoute,
            ) {
                composable(SignInGraphRoute) { SignInNavHost(viewModel = viewModel) }
            }
        }
    }

    @Test
    fun navigationTest() {
        customTestRule.signInScreen.exist()

        navigateToSignUp()
        device.pressBack() // pop backStack

        navigateToResetPassword()
        device.pressBack() // pop backStack

        customTestRule.signInScreen.exist()
    }

    @Test
    fun loadingTest() {
        fun inputEmail() = emailTextField.input(SignInEmail)
        fun inputEmailAndPassword() {
            inputEmail()
            passwordTextField(customTestRule).input(WrongPassword, invisible = true)
        }

        fun loadingCheck() {
            centerDotProgressIndicator.exist(false)
            customTestRule.waitTagNotExist(CENTER_DOT_PROGRESS_INDICATOR_TAG, 5000)
        }

        inputEmailAndPassword() // sign in loading
        signInButton.click()
        loadingCheck()

        navigateToSignUp() // sign up loading
        inputEmailAndPassword()
        nickNameTextField(customTestRule).input(SignUpName)
        passwordConfirmTextField(customTestRule).input(text = WrongPassword, invisible = true)
        signUpButton.click()
        loadingCheck()
        device.pressBack() // pop backstack

        navigateToResetPassword() // reset password loading
        inputEmail()
        sendButton.click()
        loadingCheck()
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

    @Test
    fun restPasswordGoBackTest() {
        navigateToResetPassword()
        emailTextField.input(SignInEmail)
        sendButton.click()
        customTestRule.waitTagExist(SIGN_IN_SCREEN_TAG, 5000)
    }

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

class SignUpTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)
    private val device = UiDevice.getInstance(getInstrumentation())

    @Before
    fun init() {
        FirebaseAuth.getInstance().signOut()
        customTestRule.setContent { MainActivityScreen() }
    }

    @Test
    fun signUpTest() {
        customTestRule.wait { isKeyboardShowing() }
        device.pressBack()

        customTestRule.getNodeWithStringRes(R.string.sign_up_with_email).click()
        customTestRule.getNodeWithTag(SIGN_UP_SCREEN_TAG).exist()

        emailTextField(customTestRule).input(SignUpEmail)
        nickNameTextField(customTestRule).input(SignUpName)
        passwordTextField(customTestRule).input(SignInPassword, invisible = true)
        passwordConfirmTextField(customTestRule).input(SignInPassword, invisible = true)
        signUpButton(customTestRule).click()

        customTestRule.waitTagExist(MainNavTag, 5000)

        customTestRule.wait(5000) { FirebaseAuth.getInstance().currentUser != null }

        FirebaseAuth.getInstance().currentUser!!.delete()
    }
}