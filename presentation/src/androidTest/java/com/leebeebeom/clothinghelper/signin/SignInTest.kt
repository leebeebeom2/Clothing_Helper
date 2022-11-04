package com.leebeebeom.clothinghelper.signin

import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.MainActivity
import com.leebeebeom.clothinghelper.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInTest {
    val invalidEmail = "a"
    val fakeEmail = "a@a.com"
    val email = "1@a.com"
    val fakePassword = "1"
    val password = "111111"

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            rule.onNodeWithContentDescription("settingIcon").performClick()
            rule.onNodeWithText(signOutText).performClick()
        }
    }

    @Test
    fun empty() {
        // input email
        emailField.performTextInput("a")
        signInButtonNotEnabledCheck()
        emailField.performTextClearance()

        // input password
        passwordField.performTextInput("a")
        signInButtonNotEnabledCheck()
        passwordField.performTextClearance()

        // input email, password
        emailField.performTextInput("a")
        passwordField.performTextInput("a")
        signInButtonEnabledCheck()
        clearAllText()
    }

    @Test
    fun passwordVisibility() {
        passwordField.performTextInput("a")
        rule.onNodeWithText("•").assertExists()
        openEyeIconClick()
        rule.onNodeWithText("a").assertExists()
        closeEyeIconClick()
    }

    @Test
    fun error() {

        // invalid email
        emailField.performTextInput(invalidEmail)
        passwordField.performTextInput(fakePassword)
        signInButtonClick()
        rule.onNodeWithText(getString(R.string.error_invalid_email)).assertExists() // error on
        signInButton.assertIsNotEnabled() // button not enabled
        emailField.performTextInput("aa")
        signInButton.assertIsEnabled() // button enabled
        rule.onNodeWithText(getString(R.string.error_invalid_email))
            .assertDoesNotExist() // error off
        emailField.performTextClearance()

        // fake email
        emailField.performTextInput(fakeEmail)
        signInButtonClick()
        rule.onNodeWithText(getString(R.string.error_user_not_found)).assertExists() // error on
        signInButton.assertIsNotEnabled() // button not enabled
        emailField.performTextInput("aa")
        signInButton.assertIsEnabled() // button enabled
        rule.onNodeWithText(getString(R.string.error_user_not_found))
            .assertDoesNotExist()  // error off
        clearAllText()

        // wrong password
        emailField.performTextInput(email)
        passwordField.performTextInput(fakePassword)
        signInButtonClick()
        rule.onNodeWithText(getString(R.string.error_wrong_password)).assertExists() // error on
        signInButton.assertIsNotEnabled() // button not enabled
        passwordField.performTextInput("aa")
        signInButton.assertIsEnabled() // button enabled
        rule.onNodeWithText(getString(R.string.error_wrong_password))
            .assertDoesNotExist()  // error off
        clearAllText()
    }

    @Test
    fun savable() {
        emailField.performTextInput(invalidEmail)
        passwordField.performTextInput(password)
        openEyeIconClick()
        signInButtonClick()

        rule.activityRule.scenario.recreate()

        rule.onNodeWithText(invalidEmail).assertExists()
        rule.onNodeWithText(password).assertExists()
        rule.onNodeWithText(getString(R.string.error_invalid_email)).assertExists()

        clearAllText()
    }

    @Test
    fun signInSuccess() {
        emailField.performTextInput(email)
        passwordField.performTextInput(password)
        signInButtonClick()
        rule.waitUntil { rule.onAllNodesWithText(signInText).fetchSemanticsNodes().isEmpty() }
        signInButton.assertDoesNotExist()
    }

    @Test
    fun googleSignInSuccess() {
        googleSignInButton.performClick()
        googleSignInButton.assertIsNotEnabled()
        waitLoading()
        googleSignInButton.assertDoesNotExist()
    }

    @Test
    fun navigate(){
        rule.onNodeWithText(getString(R.string.forgot_password)).performClick()
        rule.onNodeWithText(getString(R.string.reset_password_text)).assertExists()
        rule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        rule.onNodeWithText(getString(R.string.sign_up_with_email)).performClick()
        rule.onNodeWithText(getString(R.string.sign_up)).assertExists()
        rule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
    }

    fun signInButtonClick() {
        signInButton.performClick()
        waitLoading()
    }

    fun waitLoading() = rule.waitUntil(9000L) {
        rule.onAllNodesWithContentDescription("loading").fetchSemanticsNodes().isEmpty()
    }

    fun openEyeIconClick() =
        rule.onNodeWithContentDescription("openEye").performClick()

    fun closeEyeIconClick() =
        rule.onNodeWithContentDescription("closeEye").performClick()

    fun clearAllText() {
        emailField.performTextClearance()
        passwordField.performTextClearance()
    }

    fun signInButtonEnabledCheck() = signInButton.assertIsEnabled()
    fun signInButtonNotEnabledCheck() = signInButton.assertIsNotEnabled()

    val googleSignInButton get() = rule.onNodeWithText(getString(R.string.starts_with_google_email))
    val signInButton get() = rule.onNodeWithText(signInText)
    val passwordField get() = rule.onNodeWithText(passwordText)
    val emailField get() = rule.onNodeWithText(emailText)
    val passwordText get() = getString(R.string.password)
    val emailText get() = getString(R.string.email)
    val signInText get() = getString(R.string.sign_in)
    val signOutText get() = getString(R.string.sign_out)

    fun getString(@StringRes res: Int) = rule.activity.getString(res)
}