package com.leebeebeom.clothinghelper.ui

import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.MAIN_ROUTE
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.SIGN_IN_ROUTE
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는지
    @Test
    fun doesSeeSignInScreenTest() {
        if (user == null) signIn()
        rule.activity.setContent { MainActivityScreen() }
        rule.waitUntil {
            rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isEmpty()
        }
        emailTextField.assertDoesNotExist()
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
            Log.d("TAG", "assertDoesNotExistSignInBackStack: 테스트 1 통과")
        }
    }

    private fun assertDoesNotExistMainBackStack(navController: NavHostController?) {
        try {
            navController!!.getBackStackEntry(MAIN_ROUTE)
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(true)
            Log.d("TAG", "assertDoesNotExistSignInBackStack: 테스트 2 통과")
        }
    }

    private fun signOut() = FirebaseAuth.getInstance().signOut()

    private fun signIn() =
        FirebaseAuth.getInstance().signInWithEmailAndPassword("1@a.com", "111111")

    val user get() = FirebaseAuth.getInstance().currentUser
    val emailTextField get() = rule.onNodeWithText("이메일")
    fun waitSignInScreen() {
        rule.waitUntil {
            rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun waitMainScreen() {
        rule.waitUntil {
            rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isEmpty()
        }
    }
}