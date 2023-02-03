package com.leebeebeom.clothinghelper.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.ui.main.MAIN_NAV_TAG
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivitySignInStartTest {
    @get:Rule
    val rule = activityRule
    private val restoreTester = restoreTester(rule)

    @Before
    fun signIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(EMAIL, PASSWORD)
        restoreTester.setContent { MainActivityScreen() }
    }

    @Test
    fun doesSeeSignInScreenTest() {
        // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
        // TODO 구글은 따로 테스트 필요
        rule.waitMainScreen()
        rule.emailTextField.assertDoesNotExist()
        restoreTester.emulateSavedInstanceStateRestore()
        rule.emailTextField.assertDoesNotExist()
    }

    @Test
    fun signOutTest() {
        // 로그아웃 시 로그인 스크린 이동
        rule.emailTextField.assertDoesNotExist()
        rule.uiSignOut()
        rule.emailTextField.assertExists()
        restoreTester.emulateSavedInstanceStateRestore()
        rule.emailTextField.assertExists()
    }
}

class MainActivitySignOutStartTest {
    @get:Rule
    val rule = activityRule
    private val restoreTester = restoreTester(rule)
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
        rule.emailTextField.assertExists()
        rule.uiSignIn()

        assertSignIn(rule = rule, restoreTester = restoreTester)
    }

    @Test
    fun backStackTest() {
        // 로그인, 로그아웃 시 백스택은 하나여야함.
        rule.waitSignInScreen()
        assertDoesNotExistMainBackStack()

        rule.uiSignIn()
        assertDoesNotExistSignInBackStack()

        rule.uiSignOut()
        assertDoesNotExistMainBackStack()

        rule.uiSignIn()
        rule.uiSignOut()
        rule.uiSignIn()
        rule.uiSignOut()
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

private fun ComposeContentTestRule.uiSignIn() {
    emailTextField.performTextInput(EMAIL)
    passwordTextField.performTextInput(PASSWORD)
    signInButton.performClick()
    waitMainScreen()
}

private fun ComposeContentTestRule.uiSignOut() {
    onRoot().performTouchInput { swipeRight() }
    settingIcon.performClick()
    signOutButton.performClick()
    waitSignInScreen()
}

fun ComposeContentTestRule.waitSignInScreen() = waitTextNodeIsNotEmpty(text = EMAIL_LABEL)

private fun ComposeContentTestRule.waitMainScreen() =
    waitTagNodeIsNotEmpty(tag = MAIN_NAV_TAG)