package com.leebeebeom.clothinghelper.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.ui.main.drawer.SETTING_ICON
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivitySignInStartTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restoreTester = StateRestorationTester(rule)

    @Before
    fun signIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword("1@a.com", "111111")
        restoreTester.setContent { MainActivityScreen() }
    }

    @Test
    fun doesSeeSignInScreenTest() {
        // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
        // TODO 구글은 따로 테스트 필요
        waitMainScreen(rule)
        emailTextField(rule).assertDoesNotExist()
        restoreTester.emulateSavedInstanceStateRestore()
        emailTextField(rule).assertDoesNotExist()
    }

    @Test
    fun signOutTest() {
        // 로그아웃 시 로그인 스크린 이동
        emailTextField(rule).assertDoesNotExist()
        uiSignOut(rule)
        emailTextField(rule).assertExists()
        restoreTester.emulateSavedInstanceStateRestore()
        emailTextField(rule).assertExists()
    }
}

class MainActivitySignOutStartTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restoreTester = StateRestorationTester(rule)
    private lateinit var navController: NavHostController

    @Before
    fun signOut() {
        restoreTester.setContent {
            navController = rememberNavController()
            MainActivityScreen(navController = navController)
        }
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun signInTest() {
        // 로그인 시 메인 스크린 이동
        emailTextField(rule).assertExists()
        uiSignIn(rule)
        emailTextField(rule).assertDoesNotExist()
        restoreTester.emulateSavedInstanceStateRestore()
        emailTextField(rule).assertDoesNotExist()
    }

    @Test
    fun backStackTest() {
        // 로그인, 로그아웃 시 백스택은 하나여야함.
        waitSignInScreen(rule)
        assertDoesNotExistMainBackStack()

        uiSignIn(rule)
        assertDoesNotExistSignInBackStack()

        uiSignOut(rule)
        assertDoesNotExistMainBackStack()

        uiSignIn(rule)
        uiSignOut(rule)
        uiSignIn(rule)
        uiSignOut(rule)
        assertDoesNotExistMainBackStack()
    }

    private fun assertDoesNotExistSignInBackStack() {
        try {
            navController.getBackStackEntry(ActivityDestinations.SIGN_IN_ROUTE)
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(true)
        }
    }

    private fun assertDoesNotExistMainBackStack() {
        try {
            navController.getBackStackEntry(ActivityDestinations.MAIN_ROUTE)
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(true)
        }
    }
}

private const val EMAIL_TEXT = "이메일"
private const val PASSWORD_TEXT = "비밀번호"
private const val SIGN_IN_TEXT = "로그인"
private const val SIGN_OUT_TEXT = "로그아웃"

private fun emailTextField(rule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>) =
    rule.onNodeWithText(EMAIL_TEXT)

private fun passwordTextField(rule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>) =
    rule.onNodeWithText(PASSWORD_TEXT)

private fun signInButton(rule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>) =
    rule.onNodeWithText(SIGN_IN_TEXT)

fun uiSignOut(rule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>) {
    rule.onRoot().performTouchInput { swipeRight() }
    rule.onNodeWithContentDescription(SETTING_ICON).performClick()
    rule.onNodeWithText(SIGN_OUT_TEXT).performClick()
    waitSignInScreen(rule)
}

fun uiSignIn(rule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>) {
    emailTextField(rule).performTextInput("1@a.com")
    passwordTextField(rule).performTextInput("111111")
    signInButton(rule).performClick()
    waitMainScreen(rule)
}

private fun waitSignInScreen(rule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>) {
    rule.waitUntil(5000) {
        rule.onAllNodesWithText(EMAIL_TEXT).fetchSemanticsNodes().isNotEmpty()
    }
}

private fun waitMainScreen(rule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>) {
    rule.waitUntil(5000) {
        rule.onAllNodesWithText(EMAIL_TEXT).fetchSemanticsNodes().isEmpty()
    }
}