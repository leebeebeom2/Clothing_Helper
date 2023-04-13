package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.*
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicatorTag
import com.leebeebeom.clothinghelper.ui.main.main.MainScreenTag
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
    private val emailTextField by lazy { rule.onNodeWithStringRes(R.string.email) }
    private val nameTextField by lazy { rule.onNodeWithStringRes(R.string.nickname) }
    private val passwordTextField by lazy { rule.onNodeWithStringRes(R.string.password) }
    private val passwordConfirmTextField by lazy { rule.onNodeWithStringRes(R.string.password_confirm) }
    private val signUpButton by lazy { rule.onNodeWithStringRes(R.string.sign_up) }

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }
        restorationTester.setContent { MainNavHost() }
        device.pressBack()
        rule.onNodeWithStringRes(R.string.sign_up_with_email).performClick()
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
                rule.onNodeWithText(email).assertExists()
                rule.onNodeWithText(SignUpName).assertExists()
                signUpButton.assertIsNotEnabled()
                assert(
                    rule.onAllNodesWithText(getInvisibleText(password.length))
                        .fetchSemanticsNodes().size == 2
                )

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
            error = R.string.error_invalid_email
        )
        localSignUpRestoreTest(
            email = SignInEmail,
            password = SignInPassword,
            error = R.string.error_email_already_in_use
        )
    }

    @Test
    fun passwordErrorTest() { // with button enabled
        emailTextField.performTextInput(SignInEmail)
        nameTextField.performTextInput(SignUpName)

        repeat(5) {
            passwordTextField.performTextInput("1")
            rule.onNodeWithStringRes(R.string.error_weak_password).assertExists()
            rule.onNodeWithStringRes(R.string.error_password_confirm_not_same).assertDoesNotExist()
            signUpButton.assertIsNotEnabled()
        }

        repeat(4) {
            passwordConfirmTextField.performTextInput("1")
            rule.onNodeWithStringRes(R.string.error_weak_password).assertExists()
            rule.onNodeWithStringRes(R.string.error_password_confirm_not_same).assertExists()
            signUpButton.assertIsNotEnabled()
        }

        passwordConfirmTextField.performTextInput("1")
        rule.onNodeWithStringRes(R.string.error_weak_password).assertExists()
        rule.onNodeWithStringRes(R.string.error_password_confirm_not_same).assertDoesNotExist()
        signUpButton.assertIsNotEnabled()

        passwordTextField.performTextInput("1")
        rule.onNodeWithStringRes(R.string.error_weak_password).assertDoesNotExist()
        rule.onNodeWithStringRes(R.string.error_password_confirm_not_same).assertExists()
        signUpButton.assertIsNotEnabled()

        passwordConfirmTextField.performTextInput("1")
        rule.onNodeWithStringRes(R.string.error_weak_password).assertDoesNotExist()
        rule.onNodeWithStringRes(R.string.error_password_confirm_not_same).assertDoesNotExist()
        signUpButton.assertIsEnabled()

        passwordTextField.performTextClearance()
        rule.onNodeWithStringRes(R.string.error_weak_password).assertDoesNotExist()
        rule.onNodeWithStringRes(R.string.error_password_confirm_not_same).assertExists()
        signUpButton.assertIsNotEnabled()

        passwordTextField.performTextInput("111111")
        passwordConfirmTextField.performTextClearance()
        rule.onNodeWithStringRes(R.string.error_weak_password).assertDoesNotExist()
        rule.onNodeWithStringRes(R.string.error_password_confirm_not_same).assertDoesNotExist()
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
            rule.onNodeWithText(SignInEmail).assertExists()
        }

        repeat(10) {
            passwordTextField.performTextInput(" ")
            assert(
                rule.onAllNodesWithText(getInvisibleText(SignInPassword.length))
                    .fetchSemanticsNodes().size == 2
            )
        }

        repeat(10) {
            passwordConfirmTextField.performTextInput(" ")
            assert(
                rule.onAllNodesWithText(getInvisibleText(SignInPassword.length))
                    .fetchSemanticsNodes().size == 2
            )
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