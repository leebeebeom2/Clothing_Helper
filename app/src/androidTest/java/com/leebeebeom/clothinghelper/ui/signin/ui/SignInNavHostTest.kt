package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.activity.compose.setContent
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
import com.leebeebeom.clothinghelper.ui.MainActivity
import com.leebeebeom.clothinghelper.ui.components.CENTER_DOT_PROGRESS_INDICATOR_TAG
import com.leebeebeom.clothinghelper.ui.components.isKeyboardShown
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInNavHostTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private lateinit var uiState: MutableSignInNavUiState

    @Before
    fun init() {
        uiState = MutableSignInNavUiState()
        setContent()
    }

    private fun setContent() {
        rule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = SIGN_IN_ROUTE,
            ) {
                composable(SIGN_IN_ROUTE) { SignInNavHost(uiState = uiState) }
            }
        }
    }

    // centerDotProgressIndicator 테스트와 동시 진행

    @Test
    fun signInBlockBacKPressTest() {
        rule.onNodeWithText("이메일").assertExists()
        blockBackPressTest()
        rule.onNodeWithText("이메일").assertExists()
    }

    @Test
    fun signUpBlockBacKPressTest() {
        rule.waitUntil { isKeyboardShown() }
        UiDevice.getInstance(getInstrumentation()).pressBack() // 키보드 내리기
        rule.onNodeWithText("이메일로 가입하기").performClick()
        rule.onNodeWithText("닉네임").assertExists()
        blockBackPressTest()
    }

    @Test
    fun resetPasswordBlockBacKPressTest() {
        rule.onNodeWithText("비밀번호를 잊으셨나요?").performClick()
        rule.onNodeWithText("보내기").assertExists()
        blockBackPressTest()
    }

    private fun blockBackPressTest() {
        centerDotProgressIndicator.assertDoesNotExist()
        uiState.isSignInLoading = true
        centerDotProgressIndicator.assertExists()
        UiDevice.getInstance(getInstrumentation()).pressBack()
        UiDevice.getInstance(getInstrumentation()).pressBack()
        UiDevice.getInstance(getInstrumentation()).pressBack()
        centerDotProgressIndicator.assertExists()
    }

    private val centerDotProgressIndicator = rule.onNodeWithTag(CENTER_DOT_PROGRESS_INDICATOR_TAG)
}