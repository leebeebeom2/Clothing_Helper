package com.leebeebeom.clothinghelper.ui

import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        uiSignIn(rule)
        waitMainScreen(rule)
    }

    @Test
    fun backStackTest() {
        // 로그인, 로그아웃 시 백스택은 하나여야함.
        var navController: NavHostController? = null
        rule.activity.setContent {
            navController = rememberNavController()
            MainActivityScreen(navController = navController!!)
        }
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