package com.leebeebeom.clothinghelper.signin

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.leebeebeom.clothinghelper.ui.signin.SignInActivity
import org.junit.Before
import org.junit.Rule
import com.leebeebeom.clothinghelper.R
import org.junit.Test

const val NAME = "이비범"

class SignUpTest {
    @get:Rule
    val signUpScreenTestRule = createAndroidComposeRule<SignInActivity>()

    @Before
    fun setUp() {
        signUpScreenTestRule.onNodeWithText(getString(R.string.sign_up_with_email)).performClick()
    }

    @Test
    fun errorTest() {
        weakPassword()
        passwordNotSame()
    }

    private fun passwordNotSame() {
        email.performTextInput(EMAIL)
        name.performTextInput(NAME)
        password.performTextInput(PASSWORD)
        passwordConfirm.performTextInput(PASSWORD + "1")
        signUpScreenTestRule.onNodeWithText(getString(R.string.error_password_confirm_not_same))
            .assertExists()
        password.performTextInput("1")
        signUpScreenTestRule.onNodeWithText(getString(R.string.error_password_confirm_not_same))
            .assertDoesNotExist()
        password.performTextClearance()
        signUpScreenTestRule.onNodeWithText(getString(R.string.error_password_confirm_not_same))
            .assertDoesNotExist()
    }

    private fun weakPassword() {
        email.performTextInput(EMAIL)
        name.performTextInput(NAME)
        password.performTextInput("11111")
        signUpScreenTestRule.onNodeWithText(getString(R.string.error_weak_password)).assertExists()
        password.performTextInput("1")
        signUpScreenTestRule.onNodeWithText(getString(R.string.error_weak_password))
            .assertDoesNotExist()
        clearAllText()
    }

    private fun allNormal() {
        email.performTextInput(EMAIL)
        name.performTextInput(NAME)
        password.performTextInput(PASSWORD)
        passwordConfirm.performTextInput(PASSWORD)
        signUpBtn.assertIsEnabled()
        clearAllText()
    }

    private fun allEmpty() {
        clearAllText()
        signUpBtn.assertIsNotEnabled()
        clearAllText()
    }

    private fun clearAllText() {
        email.performTextClearance()
        name.performTextClearance()
        password.performTextClearance()
        passwordConfirm.performTextClearance()
    }

    private val signUpBtn get() = signUpScreenTestRule.onNodeWithText(getString(R.string.sign_up))
    private val email get() = signUpScreenTestRule.onNodeWithText(getString(R.string.email))
    private val name get() = signUpScreenTestRule.onNodeWithText(getString(R.string.name))
    private val password get() = signUpScreenTestRule.onNodeWithText(getString(R.string.password))
    private val passwordConfirm get() = signUpScreenTestRule.onNodeWithText(getString(R.string.password_confirm))

    private fun getString(resId: Int) = signUpScreenTestRule.activity.getString(resId)
}