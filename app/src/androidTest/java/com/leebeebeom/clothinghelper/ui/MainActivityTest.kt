package com.leebeebeom.clothinghelper.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.CustomTestRule
import com.leebeebeom.clothinghelper.activityRule
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.MainGraphRoute
import com.leebeebeom.clothinghelper.ui.MainActivityRoutes.SignInGraphRoute
import com.leebeebeom.clothinghelper.ui.main.MainNavTag
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavTag
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivitySignInStartTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        runBlocking {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(SignInEmail, SignInPassword)
                .await()
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
        FirebaseAuth.getInstance().signOut()
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
        rule.waitTagExit(MainNavTag)
        assert(!isBackStackExist(SignInGraphRoute))

        rule.signOut()
        rule.waitTagExit(SignInNavTag)
        assert(!isBackStackExist(MainGraphRoute))

        repeat(2) {
            rule.signIn()
            rule.waitTagExit(MainNavTag)
            rule.signOut()
            rule.waitTagExit(SignInNavTag)
        }
        assert(!isBackStackExist(MainGraphRoute))
    }
}