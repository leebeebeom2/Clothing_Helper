package com.leebeebeom.clothinghelper.ui

import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.ui.main.drawer.SETTING_ICON
import org.junit.Before
import org.junit.Rule
import org.junit.Test

typealias ActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

private val activityRule get() = createAndroidComposeRule<MainActivity>()
fun recreate(rule: ActivityRule) = rule.activityRule.scenario.recreate()

class MainActivitySignInStartTest {
    @get:Rule
    val rule = activityRule

    @Before
    fun signIn() {
        Log.d("TAG", "signIn: 실행")
        FirebaseAuth.getInstance().signInWithEmailAndPassword("1@a.com", "111111")
    }

    @Test
    fun doesSeeSignInScreenTest() {
        // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
        waitMainScreen(rule)
        emailTextField(rule).assertDoesNotExist()
        recreate(rule)
        waitMainScreen(rule)
        emailTextField(rule).assertDoesNotExist()
    }

    @Test
    fun signOutTest() {
        // 로그아웃 시 로그인 스크린 이동
        emailTextField(rule).assertDoesNotExist()
        uiSignOut(rule)
        waitSignInScreen(rule)
        recreate(rule)
        emailTextField(rule).assertExists()
    }
}

class MainActivitySignOutStartTest {
    @get:Rule
    val rule = activityRule

    @Before
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun signInTest() {
        // 로그인 시 메인 스크린 이동
        emailTextField(rule).assertExists()
        uiSignIn(rule)
        waitMainScreen(rule)
        recreate(rule)
        emailTextField(rule).assertDoesNotExist()
    }

    @Test
    fun backStackTest() {
        // 로그인, 로그아웃 시 백스택은 하나여야함.
        var navController: NavHostController? = null
        rule.activity.setContent {
            navController = rememberNavController()
            MainActivityScreen(navController = navController!!)
        }
        rule.waitForIdle()
        assertDoesNotExistMainBackStack(navController)

        uiSignIn(rule)
        waitMainScreen(rule)
        assertDoesNotExistSignInBackStack(navController)

        uiSignOut(rule)
        waitSignInScreen(rule)
        assertDoesNotExistMainBackStack(navController)

        uiSignIn(rule)
        waitMainScreen(rule)
        uiSignOut(rule)
        waitSignInScreen(rule)
        uiSignIn(rule)
        waitMainScreen(rule)
        uiSignOut(rule)
        waitSignInScreen(rule)
        assertDoesNotExistMainBackStack(navController)
    }
}

fun emailTextField(rule: ActivityRule) = rule.onNodeWithText("이메일")
fun passwordTextField(rule: ActivityRule) = rule.onNodeWithText("비밀번호")
fun signInButton(rule: ActivityRule) = rule.onNodeWithText("로그인")

fun waitSignInScreen(rule: ActivityRule) {
    rule.waitUntil(5000) {
        rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isNotEmpty()
    }
}

fun waitMainScreen(rule: ActivityRule) {
    rule.waitUntil(5000) {
        rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isEmpty()
    }
}

fun uiSignOut(rule: ActivityRule) {
    rule.onRoot().performTouchInput { swipeRight() }
    rule.onNodeWithContentDescription(SETTING_ICON).performClick()
    rule.onNodeWithText("로그아웃").performClick()
}

fun uiSignIn(rule: ActivityRule) {
    emailTextField(rule).performTextInput("1@a.com")
    passwordTextField(rule).performTextInput("111111")
    signInButton(rule).performClick()
}

private fun assertDoesNotExistSignInBackStack(navController: NavHostController?) {
    try {
        navController!!.getBackStackEntry(ActivityDestinations.SIGN_IN_ROUTE)
        assert(false)
    } catch (e: IllegalArgumentException) {
        assert(true)
    }
}

private fun assertDoesNotExistMainBackStack(navController: NavHostController?) {
    try {
        navController!!.getBackStackEntry(ActivityDestinations.MAIN_ROUTE)
        assert(false)
    } catch (e: IllegalArgumentException) {
        assert(true)
    }
}