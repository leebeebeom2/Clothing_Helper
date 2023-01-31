package com.leebeebeom.clothinghelper.ui

import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivitySignInStartTest {
    @get:Rule
    val rule = activityRule

    @Before
    fun signIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword("1@a.com", "111111")
    }

    @Test
    fun doesSeeSignInScreenTest() {
        // 로그인 상태로 앱 실행 시 로그인 화면 안 보이는 지
        rule.waitForIdle()
        emailTextField(rule).assertDoesNotExist()
    }

    @Test
    fun signOutTest() {
        // 로그아웃 시 로그인 스크린 이동
        uiSignOut(rule)
        waitSignInScreen(rule)
    }
}
