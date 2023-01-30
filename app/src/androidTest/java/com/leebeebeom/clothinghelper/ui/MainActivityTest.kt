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

    // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
    @Test
    fun doesSeeSignInScreenTest() {
        if (user == null) signIn()
        setContent()
        emailTextField.assertDoesNotExist()
    }

    // 로그인 시 메인 스크린 이동
    @Test
    fun signInTest() {
        signOut()
        setContent()
        waitSignInScreen()
        uiSignIn()
        waitMainScreen()
    }

    // 로그아웃 시 로그인 스크린 이동
    @Test
    fun signOutTest() {
        signIn()
        setContent()
        waitMainScreen()
        uiSignOut()
        waitMainScreen()
    }

    // 로그인, 로그아웃 시 백스택은 하나여야함.
    @Test
    fun backStackTest() {
        signOut()
        var navController: NavHostController? = null
        rule.activity.setContent {
            navController = rememberNavController()
            MainActivityScreen(navController = navController!!)
        }
        waitSignInScreen()
        signIn()
        waitMainScreen()
        assertDoesNotExistSignInBackStack(navController)
        signOut()
        waitSignInScreen()
        assertDoesNotExistMainBackStack(navController)
        signIn()
        signOut()
        signIn()
        signOut()
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
    }

    private fun signOut() = FirebaseAuth.getInstance().signOut()

    private fun signIn() =
        FirebaseAuth.getInstance().signInWithEmailAndPassword("1@a.com", "111111")

    val user get() = FirebaseAuth.getInstance().currentUser
    val emailTextField get() = rule.onNodeWithText("이메일")
    val passwordTextField get() = rule.onNodeWithText("비밀번호")
    val signInButton get() = rule.onNodeWithText("로그인")
    private fun waitSignInScreen() {
        rule.waitUntil {
            rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun waitMainScreen() {
        rule.waitUntil {
            rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isEmpty()
        }
    }

    private fun setContent() = rule.activity.setContent { MainActivityScreen() }
}