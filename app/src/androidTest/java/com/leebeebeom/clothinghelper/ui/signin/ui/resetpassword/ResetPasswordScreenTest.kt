package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.R.string.forgot_password
import com.leebeebeom.clothinghelper.ui.components.errorTest
import com.leebeebeom.clothinghelper.ui.components.showKeyboardTest
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavHost
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SIGN_IN_SCREEN_TAG
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResetPasswordScreenTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)

    @Before
    fun init() {
        customTestRule.setContent { SignInNavHost() }
        customTestRule.getNodeWithStringRes(forgot_password).click()
        customTestRule.waitTagExist(tag = RESET_PASSWORD_SCREEN_TAG, restore = false)
    }

    @Test
    fun showKeyboardTest() = showKeyboardTest(textField = customTestRule.emailTextField)

    @Test
    fun errorTest() {
        errorTest(
            rule = customTestRule,
            errorTextField = { customTestRule.emailTextField },
            errorTextRes = R.string.error_invalid_email,
            setError = {
                customTestRule.emailTextField.input(INVALID_EMAIL)
                customTestRule.sendButton.click()
            },
            beforeText = INVALID_EMAIL
        )

        errorTest(
            rule = customTestRule,
            errorTextField = { customTestRule.emailTextField },
            errorTextRes = R.string.error_user_not_found,
            setError = {
                customTestRule.emailTextField.input(NOT_EXIST_EMAIL)
                customTestRule.sendButton.click()
            },
            beforeText = NOT_EXIST_EMAIL
        )
    }

    @Test
    fun goBackTest() {
        customTestRule.emailTextField.input(EMAIL)
        customTestRule.sendButton.click()
        customTestRule.waitTagExist(SIGN_IN_SCREEN_TAG, 5000)
    }

//    @Test TODO 테스트 불가
//    fun looseFocusTest() =
//        looseFocusTest(
//            textField = { customTestRule.emailTextField },
//            root = { customTestRule.getNodeWithTag(RESET_PASSWORD_SCREEN_TAG) }
//        )
}