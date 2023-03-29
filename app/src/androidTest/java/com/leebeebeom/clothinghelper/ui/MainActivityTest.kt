package com.leebeebeom.clothinghelper.ui

import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.MainGraphRoute
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.SignInGraphRoute
import com.leebeebeom.clothinghelper.ui.drawer.component.SettingIconTag
import com.leebeebeom.clothinghelper.ui.main.mainScreen.MainScreenTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreenTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }

        restorationTester.setContent {
            MainNavHost()
        }
    }

    @Test
    fun restoreTest() {
        repeat(2) {
            rule.onNodeWithTag(SignInScreenTag).assertExists()
            rule.onRoot().performTouchInput { swipeRight() }
            rule.onNodeWithTag(SettingIconTag).assertDoesNotExist()

            restorationTester.emulateSavedInstanceStateRestore()
        }

        rule.signIn()

        repeat(2) {
            rule.waitTagExist(MainScreenTag)
            rule.onRoot().performTouchInput { swipeRight() }
            rule.onNodeWithTag(SettingIconTag).assertExists()

            restorationTester.emulateSavedInstanceStateRestore()
        }

        rule.signOut()

        repeat(2) {
            rule.waitTagExist(SignInScreenTag)
            rule.onRoot().performTouchInput { swipeRight() }
            rule.onNodeWithTag(SettingIconTag).assertDoesNotExist()

            restorationTester.emulateSavedInstanceStateRestore()
        }
    }
}

class MainActivitySignInStartTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signInWithEmailAndPassword(SignInEmail, SignInPassword).await()
            delay(1000)
        }
        rule.setContent { MainNavHost() }
    }

    // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
    // TODO 구글은 따로 테스트 필요
    @Test
    fun mainScreenTest() {
        rule.onNodeWithTag(MainScreenTag).assertExists()
    }
}

class MainActivitySignOutStartTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private lateinit var navController: NavHostController

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }
        rule.setContent {
            navController = rememberNavController()
            MainNavHost(navController = navController)
        }
    }

    @Test
    fun signInScreenTest() {
        rule.onNodeWithTag(SignInScreenTag).assertExists()
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

        rule.signIn()
        rule.waitTagExist(MainScreenTag)
        assert(!isBackStackExist(SignInGraphRoute))

        rule.signOut()
        rule.waitTagExist(SignInScreenTag)
        assert(!isBackStackExist(MainGraphRoute))

        repeat(2) {
            rule.signIn()
            rule.waitTagExist(MainScreenTag)
            rule.signOut()
            rule.waitTagExist(SignInScreenTag)
        }
        assert(!isBackStackExist(MainGraphRoute))
    }
}