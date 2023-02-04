package com.leebeebeom.clothinghelper.ui

import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.ui.main.MAIN_NAV_TAG
import com.leebeebeom.clothinghelper.ui.signin.ui.SIGN_IN_NAV_TAG
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivitySignInStartTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule = rule)

    @Before
    fun init() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(EMAIL, PASSWORD)
        customTestRule.setContent { MainActivityScreen() }
    }

    // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
    // TODO 구글은 따로 테스트 필요
    @Test
    fun doesSeeSignInScreenTest() = customTestRule.waitMainNav()

    @Test
    fun signOutTest() {
        // 로그아웃 시 로그인 스크린 이동
        customTestRule.uiSignOut()
        customTestRule.waitSignInNav()
    }
}

class MainActivitySignOutStartTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule = rule)
    private lateinit var navController: NavHostController

    @Before
    fun init() {
        FirebaseAuth.getInstance().signOut()
        customTestRule.setContent {
            navController = rememberNavController()
            MainActivityScreen(navController = navController)
        }
    }

    @Test
    fun signInTest() = customTestRule.uiSignIn()

    @Test
    fun backStackTest() {
        // 로그인, 로그아웃 시 백스택은 하나여야함.
        notExistMainBackStack()

        customTestRule.uiSignIn()
        notExistSignInBackStack()

        customTestRule.uiSignOut()
        notExistMainBackStack()

        customTestRule.uiSignIn()
        customTestRule.uiSignOut()
        customTestRule.uiSignIn()
        customTestRule.uiSignOut()
        notExistMainBackStack()
    }

    private fun notExistSignInBackStack() {
        try {
            navController.getBackStackEntry(ActivityDestinations.SIGN_IN_ROUTE)
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(true)
        }
    }

    private fun notExistMainBackStack() {
        try {
            navController.getBackStackEntry(ActivityDestinations.MAIN_ROUTE)
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(true)
        }
    }
}

private fun CustomTestRule.uiSignIn() {
    emailTextField.input(EMAIL)
    passwordTextField.invisibleInput(PASSWORD)
    signInButton.click()
    centerDotProgressIndicator.exist(false)
    waitMainNav()
}

private fun CustomTestRule.uiSignOut() {
    root.performTouchInput { swipeRight() }
    settingIcon.click()
    signOutButton.click()
    waitSignInNav()
}

private fun CustomTestRule.waitSignInNav() = waitTagExist(tag = SIGN_IN_NAV_TAG)
private fun CustomTestRule.waitMainNav() = waitTagExist(tag = MAIN_NAV_TAG)