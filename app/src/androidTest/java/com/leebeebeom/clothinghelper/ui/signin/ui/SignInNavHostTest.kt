package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.leebeebeom.clothinghelper.activityRule
import com.leebeebeom.clothinghelper.centerDotProgressIndicator
import com.leebeebeom.clothinghelper.keyboardCheck
import com.leebeebeom.clothinghelper.restoreTester
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.SIGN_IN_ROUTE
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInNavHostTest {
    @get:Rule
    val rule = activityRule
    private val restoreTester = restoreTester(rule)
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
    fun signInBlockBacKPressTest() = blockBackPressTest(restoreTester = restoreTester)

    @Test
    fun signUpBlockBacKPressTest() {
        rule.waitUntil { keyboardCheck() }
        device.pressBack() // 키보드 내리기
        rule.onNodeWithText("이메일로 가입하기").performClick()
        blockBackPressTest(restoreTester = restoreTester)
    }

    @Test
    fun resetPasswordBlockBacKPressTest() {
        rule.onNodeWithText("비밀번호를 잊으셨나요?").performClick()
        blockBackPressTest(restoreTester = restoreTester)
    }

    private fun blockBackPressTest(restoreTester: StateRestorationTester) {
        blockBackPressTest()
        restoreTester.emulateSavedInstanceStateRestore()
        blockBackPressTest()
    }

    private fun blockBackPressTest() {
        rule.centerDotProgressIndicator.assertDoesNotExist()
        uiState.isSignInLoading = true
        rule.centerDotProgressIndicator.assertExists()
        device.pressBack()
        device.pressBack()
        device.pressBack()
        device.pressBack()
        rule.centerDotProgressIndicator.assertExists()
        uiState.isSignInLoading = false
    }
}