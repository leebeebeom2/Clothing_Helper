package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.SIGN_IN_ROUTE
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.components.CENTER_DOT_PROGRESS_INDICATOR_TAG
import com.leebeebeom.clothinghelper.ui.components.isKeyboardShown
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInNavHostTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restoreTester = StateRestorationTester(rule)
    private val device = UiDevice.getInstance(getInstrumentation())

    private lateinit var uiState: MutableSignInNavUiState

    @Before
    fun init() {
        uiState = MutableSignInNavUiState()
        restoreTester.setContent {
            NavHost(
                navController = rememberNavController(),
                startDestination = SIGN_IN_ROUTE,
            ) {
                composable(SIGN_IN_ROUTE) { SignInNavHost(uiState = uiState) }
            }
        }
    }

    // centerDotProgressIndicator 테스트와 동시 진행
    // 네비게이션 테스트 포함
    @Test
    fun signInBlockBacKPressTest() {
        val emailTextField = rule.onNodeWithText("이메일")

        emailTextField.assertExists()
        blockBackPressTest()
        emailTextField.assertExists()
    }

    @Test
    fun signUpBlockBacKPressTest() {
        rule.waitUntil { isKeyboardShown() }
        device.pressBack() // 키보드 내리기
        rule.onNodeWithText("이메일로 가입하기").performClick() // 이동

        val nickNameTextField = rule.onNodeWithText("닉네임")
        nickNameTextField.assertExists()
        blockBackPressTest()
        nickNameTextField.assertExists()
    }

    @Test
    fun resetPasswordBlockBacKPressTest() {
        rule.onNodeWithText("비밀번호를 잊으셨나요?").performClick()

        val sendButton = rule.onNodeWithText("보내기")
        sendButton.assertExists()
        blockBackPressTest()
        sendButton.assertExists()
    }

    private fun blockBackPressTest() {
        centerDotProgressIndicator.assertDoesNotExist()
        uiState.isSignInLoading = true
        centerDotProgressIndicator.assertExists()
        device.pressBack()
        device.pressBack()
        device.pressBack()
        centerDotProgressIndicator.assertExists()
    }

    private val centerDotProgressIndicator = rule.onNodeWithTag(CENTER_DOT_PROGRESS_INDICATOR_TAG)
}