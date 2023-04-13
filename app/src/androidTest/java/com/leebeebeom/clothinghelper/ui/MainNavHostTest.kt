package com.leebeebeom.clothinghelper.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.ui.drawer.component.SettingIconTag
import com.leebeebeom.clothinghelper.ui.main.main.MainScreenTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInScreenTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainNavHostTest {
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
            rule.onNodeWithTag(SettingIconTag).assertIsNotDisplayed()

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
        rule.onNodeWithTag(SettingIconTag).assertIsNotDisplayed() // drawer automatic close

        repeat(2) {
            rule.waitTagExist(SignInScreenTag)
            rule.onRoot().performTouchInput { swipeRight() }
            rule.onNodeWithTag(SettingIconTag).assertIsNotDisplayed()

            restorationTester.emulateSavedInstanceStateRestore()
        }
    }
}

class MainNavHostSignInStartTest {
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

class MainNavHostSignOutStartTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }
        rule.setContent { MainNavHost() }
    }

    @Test
    fun signInScreenTest() {
        rule.onNodeWithTag(SignInScreenTag).assertExists()
    }
}