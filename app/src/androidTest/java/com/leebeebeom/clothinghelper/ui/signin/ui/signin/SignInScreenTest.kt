package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.InvalidEmail
import com.leebeebeom.clothinghelper.data.NotFoundEmail
import com.leebeebeom.clothinghelper.data.SignInEmail
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicatorTag
import com.leebeebeom.clothinghelper.ui.main.main.MainScreenTag
import com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword.ResetPasswordScreenTag
import com.leebeebeom.clothinghelper.ui.signin.ui.signup.SignUpScreenTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val emailTextField by lazy { rule.onNodeWithStringRes(R.string.email) }
    private val passwordTextField by lazy { rule.onNodeWithStringRes(R.string.password) }
    private val signInButton by lazy { rule.onNodeWithStringRes(R.string.sign_in) }

    @Before
    fun init() {
        signOut()
        restorationTester.setContent { MainNavHost() }
    }

    @After
    fun signOut() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }
    }

    @Test
    fun signInRestoreTest() { // with error, loading, button enabled
        fun localSignInRestoreTest(
            email: String, password: String, @StringRes error: Int
        ) {
            emailTextField.performTextInput(email)
            passwordTextField.performTextInput(password)
            signInButton.performClick()
            rule.waitTagExist(CenterDotProgressIndicatorTag)

            repeat(2) {
                rule.waitStringResExist(error)
                rule.onNodeWithText(email).assertExists()
                rule.onNodeWithText(getInvisibleText(password.length)).assertExists()
                signInButton.assertIsNotEnabled()

                restorationTester.emulateSavedInstanceStateRestore()
            }

            emailTextField.performTextClearance()
            passwordTextField.performTextClearance()
        }

        signInButton.assertIsNotEnabled()

        localSignInRestoreTest(
            email = InvalidEmail, password = SignInPassword, error = R.string.error_invalid_email
        )

        localSignInRestoreTest(
            email = NotFoundEmail, password = SignInPassword, error = R.string.error_user_not_found
        )

        localSignInRestoreTest(
            email = SignInEmail, password = "123456", error = R.string.error_wrong_password
        )

        emailTextField.performTextInput(SignInEmail)
        passwordTextField.performTextInput(SignInPassword)

        repeat(2) {
            rule.onNodeWithText(SignInEmail).assertExists()
            rule.onNodeWithText(getInvisibleText(SignInPassword.length)).assertExists()
            signInButton.assertIsEnabled()

            restorationTester.emulateSavedInstanceStateRestore()
        }
    }

    @Test
    fun navigateTest() {
        rule.onNodeWithStringRes(R.string.forgot_password).performClick()
        rule.onNodeWithTag(ResetPasswordScreenTag).assertExists()
        repeat(2) { device.pressBack() }
        rule.onNodeWithStringRes(R.string.sign_up_with_email).performClick()
        rule.onNodeWithTag(SignUpScreenTag).assertExists()
        repeat(2) { device.pressBack() }
        rule.onNodeWithTag(SignInScreenTag).assertExists()
    }

    @Test
    fun signInTest() {
        emailTextField.performTextInput(SignInEmail)
        passwordTextField.performTextInput(SignInPassword)
        signInButton.performClick()

        rule.waitTagExist(MainScreenTag)
    }

    @Test
    fun buttonEnabledTest() {
        signInButton.assertIsNotEnabled()

        emailTextField.performTextInput(InvalidEmail)
        signInButton.assertIsNotEnabled()

        passwordTextField.performTextInput(SignInPassword)
        signInButton.assertIsEnabled()

        emailTextField.performTextClearance()
        signInButton.assertIsNotEnabled()

        emailTextField.performTextInput(InvalidEmail)
        signInButton.assertIsEnabled()

        signInButton.performClick()

        rule.waitStringResExist(R.string.error_invalid_email)
        signInButton.assertIsNotEnabled()

        emailTextField.performTextInput("0")
        signInButton.assertIsEnabled()
    }

    @Test
    fun signInBlockBlankTest() {
        emailTextField.performTextInput(SignInEmail)
        passwordTextField.performTextInput(SignInPassword)

        repeat(10) {
            emailTextField.performTextInput(" ")
            rule.onNodeWithText(SignInEmail).assertExists()
        }

        repeat(10) {
            passwordTextField.performTextInput(" ")
            rule.onNodeWithText(getInvisibleText(SignInPassword.length)).assertExists()
        }
    }
}