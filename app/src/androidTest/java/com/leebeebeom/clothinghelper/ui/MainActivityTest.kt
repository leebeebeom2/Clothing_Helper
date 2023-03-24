package com.leebeebeom.clothinghelper.ui

import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.MainGraphRoute
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.SignInGraphRoute
import com.leebeebeom.clothinghelper.ui.main.MainNavTag
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavTag
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
            MainActivityScreen()
        }
    }

    @Test
    fun restoreTest() {
        restorationTester.emulateSavedInstanceStateRestore()
        rule.onNodeWithTag(SignInNavTag).assertExists()
        rule.signIn()
        rule.waitTagExist(MainNavTag)
        restorationTester.emulateSavedInstanceStateRestore()
        rule.onNodeWithTag(MainNavTag).assertExists()
        rule.signOut()
        rule.waitTagExist(SignInNavTag)
        restorationTester.emulateSavedInstanceStateRestore()
        rule.onNodeWithTag(SignInNavTag).assertExists()
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
        rule.setContent { MainActivityScreen() }
    }

    // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
    // TODO 구글은 따로 테스트 필요
    @Test
    fun mainScreenTest() {
        rule.onNodeWithTag(SignInNavTag).assertDoesNotExist()
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
            MainActivityScreen(navController = navController)
        }
    }

    @Test
    fun signInScreenTest() {
        rule.onNodeWithTag(SignInNavTag).assertExists()
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
        rule.waitTagExist(MainNavTag)
        assert(!isBackStackExist(SignInGraphRoute))

        rule.signOut()
        rule.waitTagExist(SignInNavTag)
        assert(!isBackStackExist(MainGraphRoute))

        repeat(2) {
            rule.signIn()
            rule.waitTagExist(MainNavTag)
            rule.signOut()
            rule.waitTagExist(SignInNavTag)
        }
        assert(!isBackStackExist(MainGraphRoute))
    }
}