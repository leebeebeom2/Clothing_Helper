package com.leebeebeom.clothinghelper.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.main.drawer.SETTING_ICON
import org.junit.Rule
import org.junit.Test

typealias ActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

val activityRule get() = createAndroidComposeRule<MainActivity>()

fun emailTextField(rule: ActivityRule) = rule.onNodeWithText("이메일")
fun passwordTextField(rule: ActivityRule) = rule.onNodeWithText("비밀번호")
fun signInButton(rule: ActivityRule) = rule.onNodeWithText("로그인")

fun waitSignInScreen(rule: ActivityRule) {
    rule.waitUntil(5000) {
        rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isNotEmpty()
    }
}

fun waitMainScreen(rule: ActivityRule) {
    rule.waitUntil(5000) {
        rule.onAllNodesWithText("이메일").fetchSemanticsNodes().isEmpty()
    }
}

fun uiSignOut(rule: ActivityRule) {
    rule.onRoot().performTouchInput { swipeRight() }
    rule.onNodeWithContentDescription(SETTING_ICON).performClick()
    rule.onNodeWithText("로그아웃").performClick()
}

fun uiSignIn(rule: ActivityRule) {
    emailTextField(rule).performTextInput("1@a.com")
    passwordTextField(rule).performTextInput("111111")
    signInButton(rule).performClick()
}

class MainActivityTest {
    @get:Rule
    val rule = activityRule

    @Test
    fun toastTest() {
        var viewModel: ActivityViewModel? = null
        rule.activity.setContent {
            viewModel = activityViewModel()
            MainActivityScreen(viewModel = viewModel!!)
        }
        rule.waitForIdle()
        viewModel!!.showToast(R.string.toast_test)
        rule.waitForIdle()
        // 테스트 불가
    }
}