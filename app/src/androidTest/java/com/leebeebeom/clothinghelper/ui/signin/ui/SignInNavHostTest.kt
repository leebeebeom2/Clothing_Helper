package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.SIGN_IN_ROUTE
import com.leebeebeom.clothinghelper.ui.MainActivityScreen
import com.leebeebeom.clothinghelper.ui.components.CENTER_DOT_PROGRESS_INDICATOR_TAG
import com.leebeebeom.clothinghelper.ui.components.showKeyboardOnceTest
import com.leebeebeom.clothinghelper.ui.main.MAIN_NAV_TAG
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.RESET_PASSWORD_SCREEN_TAG
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SIGN_IN_SCREEN_TAG
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SIGN_UP_SCREEN_TAG
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// TODO 구글 로그인 테스트
// TODO 구글 이메일 가입 테스트 시 이메일 등록 및 데이터 생성 확인

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
        device.pressBack() // pop backStack

        navigateToResetPassword()
        device.pressBack() // pop backStack

        customTestRule.signInScreen.exist()
    }

    @Test
    fun loadingTest() {
        fun inputEmail() = emailTextField.input(EMAIL)
        fun inputEmailAndPassword() {
            inputEmail()
            passwordTextField(customTestRule).input(WRONG_PASSWORD, invisible = true)
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
        nickNameTextField(customTestRule).input(NICK_NAME)
        passwordConfirmTextField(customTestRule).input(text = WRONG_PASSWORD, invisible = true)
        signUpButton.click()
        loadingCheck()
        device.pressBack() // pop backstack

        navigateToResetPassword() // reset password loading
        inputEmail()
        sendButton.click()
        loadingCheck()
    }

    @Test
    fun blockBackPressTest() {
        fun innerBlockBackPressTest() {
            customTestRule.restore {
                val uiState = viewModel.uiState as MutableSignInNavUiState

                centerDotProgressIndicator.notExist()
                uiState.isSignInLoading = true
                centerDotProgressIndicator.exist()
                repeat(4) { device.pressBack() }
                centerDotProgressIndicator.exist()
                uiState.isSignInLoading = false
                centerDotProgressIndicator.notExist()
            }
        }

        innerBlockBackPressTest()

        navigateToSignUp()
        innerBlockBackPressTest()
        device.pressBack()

        navigateToResetPassword()
        innerBlockBackPressTest()
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
        emailTextField.input(EMAIL)
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
        customTestRule.wait { keyboardCheck() }
        device.pressBack()

        customTestRule.getNodeWithStringRes(R.string.sign_up_with_email).click()
        customTestRule.getNodeWithTag(SIGN_UP_SCREEN_TAG).exist()

        emailTextField(customTestRule).input(NOT_EXIST_EMAIL)
        nickNameTextField(customTestRule).input(NICK_NAME)
        passwordTextField(customTestRule).input(PASSWORD, invisible = true)
        passwordConfirmTextField(customTestRule).input(PASSWORD, invisible = true)
        signUpButton(customTestRule).click()

        customTestRule.waitTagExist(MAIN_NAV_TAG, 5000)

        customTestRule.wait(5000) { FirebaseAuth.getInstance().currentUser != null }

        val user = FirebaseAuth.getInstance().currentUser!!
        val uid = user.uid
        user.delete()
        Firebase.database.getReference(uid).removeValue()
    }
}