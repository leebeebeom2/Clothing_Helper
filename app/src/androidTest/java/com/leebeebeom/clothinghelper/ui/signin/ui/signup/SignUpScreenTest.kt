package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.R.string.*
import com.leebeebeom.clothinghelper.data.*
import com.leebeebeom.clothinghelper.getInvisibleText
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.MainNavHost
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicatorTag
import com.leebeebeom.clothinghelper.ui.main.essentialmenu.main.MainScreenTag
import com.leebeebeom.clothinghelper.waitStringResExist
import com.leebeebeom.clothinghelper.waitTagExist
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// TODO 구글 로그인 테스트

class SignUpScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val emailTextField = rule.onNodeWithStringRes(email)
    private val nameTextField = rule.onNodeWithStringRes(nickname)
    private val passwordTextField = rule.onNodeWithStringRes(password)
    private val passwordConfirmTextField = rule.onNodeWithStringRes(password_confirm)
    private val signUpButton = rule.onNodeWithStringRes(sign_up)

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }
        restorationTester.setContent { MainNavHost() }
        device.pressBack()
        rule.onNodeWithStringRes(sign_up_with_email).performClick()
    }

    @Test
    fun signUpRestoreTest() { // with loading, error, button enabled
        fun localSignUpRestoreTest(
            email: String,
            password: String,
            @StringRes error: Int,
        ) {
            emailTextField.performTextInput(email)
            nameTextField.performTextInput(SignUpName)
            passwordTextField.performTextInput(password)
            passwordConfirmTextField.performTextInput(password)
            signUpButton.performClick()
            rule.waitTagExist(CenterDotProgressIndicatorTag)

            repeat(2) {
                rule.waitStringResExist(error)
                emailTextField.assert(hasText(email))
                nameTextField.assert(hasText(SignUpName))
                passwordTextField.assert(hasText(getInvisibleText(password.length)))
                passwordConfirmTextField.assert(hasText(getInvisibleText(password.length)))
                signUpButton.assertIsNotEnabled()

                restorationTester.emulateSavedInstanceStateRestore()
            }

            emailTextField.performTextClearance()
            nameTextField.performTextClearance()
            passwordTextField.performTextClearance()
            passwordConfirmTextField.performTextClearance()
        }

        signUpButton.assertIsNotEnabled()

        localSignUpRestoreTest(
            email = InvalidEmail,
            password = SignInPassword,
            error = error_invalid_email
        )
        localSignUpRestoreTest(
            email = SignInEmail,
            password = SignInPassword,
            error = error_email_already_in_use
        )
    }

    @Test
    fun passwordErrorTest() { // with button enabled
        emailTextField.performTextInput(SignInEmail)
        nameTextField.performTextInput(SignUpName)

        val weakPasswordError = rule.onNodeWithStringRes(error_weak_password)
        val passwordNotSameError = rule.onNodeWithStringRes(error_password_confirm_not_same)

        repeat(5) {
            passwordTextField.performTextInput("1")
            weakPasswordError.assertExists()
            passwordNotSameError.assertDoesNotExist()
            signUpButton.assertIsNotEnabled()
        }

        repeat(4) {
            passwordConfirmTextField.performTextInput("1")
            weakPasswordError.assertExists()
            passwordNotSameError.assertExists()
            signUpButton.assertIsNotEnabled()
        }

        passwordConfirmTextField.performTextInput("1")
        weakPasswordError.assertExists()
        passwordNotSameError.assertDoesNotExist()
        signUpButton.assertIsNotEnabled()

        passwordTextField.performTextInput("1")
        weakPasswordError.assertDoesNotExist()
        passwordNotSameError.assertExists()
        signUpButton.assertIsNotEnabled()

        passwordConfirmTextField.performTextInput("1")
        weakPasswordError.assertDoesNotExist()
        passwordNotSameError.assertDoesNotExist()
        signUpButton.assertIsEnabled()

        passwordTextField.performTextClearance()
        weakPasswordError.assertDoesNotExist()
        passwordNotSameError.assertExists()
        signUpButton.assertIsNotEnabled()

        passwordTextField.performTextInput("111111")
        passwordConfirmTextField.performTextClearance()
        weakPasswordError.assertDoesNotExist()
        passwordNotSameError.assertDoesNotExist()
        signUpButton.assertIsNotEnabled()
    }

    @Test
    fun buttonEnabledTest() {
        val inputs = arrayOf(
            { emailTextField.performTextInput(SignInEmail) },
            { nameTextField.performTextInput(SignUpName) },
            { passwordTextField.performTextInput(SignInPassword) },
            { passwordConfirmTextField.performTextInput(SignInPassword) }
        )

        val nodes =
            arrayOf(emailTextField, nameTextField, passwordTextField, passwordConfirmTextField)

        repeat(10) {
            inputs.shuffle()
            inputs.forEachIndexed { index, input ->
                input()
                if (index < 3) signUpButton.assertIsNotEnabled()
                else signUpButton.assertIsEnabled()
            }
            nodes.shuffle()
            nodes.forEach {
                it.performTextClearance()
                signUpButton.assertIsNotEnabled()
            }
        }
    }

    @Test
    fun signUpBlockBlankTest() {
        emailTextField.performTextInput(SignInEmail)
        passwordTextField.performTextInput(SignInPassword)
        passwordConfirmTextField.performTextInput(SignInPassword)

        repeat(10) {
            emailTextField.performTextInput(" ")
            emailTextField.assert(hasText(SignInEmail))
        }

        repeat(10) {
            passwordTextField.performTextInput(" ")
            passwordTextField.assert(hasText(getInvisibleText(SignInPassword.length)))
        }

        repeat(10) {
            passwordConfirmTextField.performTextInput(" ")
            passwordConfirmTextField.assert(hasText(getInvisibleText(SignInPassword.length)))
        }
    }

    @Test
    fun signUpNameBlankTest() { // with SignUp
        emailTextField.performTextInput(SignUpEmail)
        passwordTextField.performTextInput(SignInPassword)
        passwordConfirmTextField.performTextInput(SignInPassword)

        nameTextField.performTextInput("  $SignUpName  ")

        signUpButton.performClick()

        rule.waitTagExist(MainScreenTag)
        runBlocking { delay(5000) }
        assert(Firebase.auth.currentUser?.displayName == SignUpName)

        Firebase.auth.currentUser?.delete()
    }
}