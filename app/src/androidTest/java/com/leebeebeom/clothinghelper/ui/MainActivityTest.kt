package com.leebeebeom.clothinghelper.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.MAIN_ROUTE
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.SIGN_IN_ROUTE
import com.leebeebeom.clothinghelper.ui.main.drawer.SETTING_ICON
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun doesSeeSignInScreenTest() {
        // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
        firebaseSignIn()
        rule.waitForIdle()
        emailTextField.assertDoesNotExist()
    }

    @Test
    fun signInTest() {
        // 로그인 시 메인 스크린 이동
        firebaseSignOut()
        waitSignInScreen()
        uiSignIn()
        waitMainScreen()
    }

    @Test
    fun signOutTest() {
        // 로그아웃 시 로그인 스크린 이동
        firebaseSignIn()
        waitMainScreen()
        uiSignOut()
        waitSignInScreen()
    }

    @Test
    fun backStackTest() {
        // 로그인, 로그아웃 시 백스택은 하나여야함.
        firebaseSignOut()
        var navController: NavHostController? = null
        rule.activity.setContent {
            navController = rememberNavController()
            MainActivityScreen(navController = navController!!)
        }
        waitSignInScreen()

        uiSignIn()
        waitMainScreen()
        assertDoesNotExistSignInBackStack(navController)

        uiSignOut()
        waitSignInScreen()
        assertDoesNotExistMainBackStack(navController)

        uiSignIn()
        waitMainScreen()
        uiSignOut()
        waitSignInScreen()
        uiSignIn()
        waitMainScreen()
        uiSignOut()
        waitSignInScreen()
        assertDoesNotExistMainBackStack(navController)
    }

    private fun assertDoesNotExistSignInBackStack(navController: NavHostController?) {
        try {
            navController!!.getBackStackEntry(SIGN_IN_ROUTE)
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(true)
        }
    }

    private fun assertDoesNotExistMainBackStack(navController: NavHostController?) {
        try {
            navController!!.getBackStackEntry(MAIN_ROUTE)
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(true)
        }
    }

    private fun uiSignIn() {
        emailTextField.performTextInput("1@a.com")
        passwordTextField.performTextInput("111111")
        signInButton.performClick()
    }

    private fun uiSignOut() {
        rule.onRoot().performTouchInput { swipeRight() }
        rule.onNodeWithContentDescription(SETTING_ICON).performClick()
        rule.onNodeWithText("로그아웃").performClick()
    }

    private fun firebaseSignOut() = FirebaseAuth.getInstance().signOut()

    private fun firebaseSignIn() =
        FirebaseAuth.getInstance().signInWithEmailAndPassword("1@a.com", "111111")

    private val emailTextField get() = rule.onNodeWithText("이메일")
    private val passwordTextField get() = rule.onNodeWithText("비밀번호")
    private val signInButton get() = rule.onNodeWithText("로그인")
    private fun waitSignInScreen() {
        rule.waitUntil(5000) {
            rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun waitMainScreen() {
        rule.waitUntil(5000) {
            rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isEmpty()
        }
    }
}