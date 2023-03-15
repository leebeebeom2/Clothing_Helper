package com.leebeebeom.clothinghelper.ui

import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.CustomTestRule
import com.leebeebeom.clothinghelper.EMAIL
import com.leebeebeom.clothinghelper.PASSWORD
import com.leebeebeom.clothinghelper.R.string.*
import com.leebeebeom.clothinghelper.activityRule
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.MainGraphRoute
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.SignInGraphRoute
import com.leebeebeom.clothinghelper.ui.components.CENTER_DOT_PROGRESS_INDICATOR_TAG
import com.leebeebeom.clothinghelper.ui.main.MainNavTag
import com.leebeebeom.clothinghelper.ui.main.drawer.SETTING_ICON
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavTag
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivitySignInStartTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule = rule)

    @Before
    fun init() {
        runBlocking { FirebaseAuth.getInstance().signInWithEmailAndPassword(EMAIL, PASSWORD).await() }
        customTestRule.setContent { MainActivityScreen() }
    }

    // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
    // TODO 구글은 따로 테스트 필요
    @Test
    fun mainScreenTest() {
        customTestRule.getNodeWithTag(SignInNavTag).notExist()
    }

    @Test
    fun signOutTest() {
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
    fun signInScreenTest() {
        customTestRule.getNodeWithTag(SignInNavTag).exist()
    }

    @Test
    fun signInTest() {
        customTestRule.uiSignIn()
        customTestRule.waitMainNav()
    }

    @Test
    fun backStackTest() {
        fun isBackStackExist(route: String): Boolean {
            return try {
                navController.getBackStackEntry(route)
                true
            } catch (e: Exception) {
                false
            }
        }
        // 로그인, 로그아웃 시 백스택은 하나여야함.
        assert(!isBackStackExist(MainGraphRoute))

        customTestRule.uiSignIn()
        customTestRule.waitMainNav()
        assert(!isBackStackExist(SignInGraphRoute))

        customTestRule.uiSignOut()
        customTestRule.waitSignInNav()
        assert(!isBackStackExist(MainGraphRoute))

        repeat(2) {
            customTestRule.uiSignIn()
            customTestRule.waitMainNav()
            customTestRule.uiSignOut()
            customTestRule.waitSignInNav()
        }
        assert(!isBackStackExist(MainGraphRoute))
    }
}

private fun CustomTestRule.uiSignIn() {
    getNodeWithStringRes(email).input(EMAIL)
    getNodeWithStringRes(password).input(PASSWORD, invisible = true)
    getNodeWithStringRes(sign_in).click()
    getNodeWithTag(CENTER_DOT_PROGRESS_INDICATOR_TAG).exist(false)
}

private fun CustomTestRule.uiSignOut() {
    root.performTouchInput { swipeRight() }
    getNodeWithDescription(SETTING_ICON).click()
    getNodeWithStringRes(sign_out).click()
}

private fun CustomTestRule.waitSignInNav() = waitTagExist(tag = SignInNavTag)
private fun CustomTestRule.waitMainNav() = waitTagExist(tag = MainNavTag)