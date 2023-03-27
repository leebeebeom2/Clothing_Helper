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
import com.leebeebeom.clothinghelper.ui.components.CenterDotProgressIndicatorTag
import com.leebeebeom.clothinghelper.ui.main.mainScreen.MainScreenTag
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
    private lateinit var emailTextField: SemanticsNodeInteraction
    private lateinit var nameTextField: SemanticsNodeInteraction
    private lateinit var passwordTextField: SemanticsNodeInteraction
    private lateinit var passwordConfirmTextField: SemanticsNodeInteraction
    private lateinit var signUpButton: SemanticsNodeInteraction

    @Before
    fun init() {
        runBlocking {
            Firebase.auth.signOut()
            delay(1000)
        }
        restorationTester.setContent { MainNavHost() }
        emailTextField = rule.onNodeWithStringRes(R.string.email)
        nameTextField = rule.onNodeWithStringRes(R.string.nickname)
        passwordTextField = rule.onNodeWithStringRes(R.string.password)
        passwordConfirmTextField = rule.onNodeWithStringRes(R.string.password_confirm)
        signUpButton = rule.onNodeWithStringRes(R.string.sign_up)
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
                rule.onNodeWithText(email)
                rule.onNodeWithText(SignUpName)
                signUpButton.assertIsNotEnabled()
                assert(
                    rule.onAllNodesWithText(getInvisibleText(password.length))
                        .fetchSemanticsNodes().size == 2
                )

                restorationTester.emulateSavedInstanceStateRestore()
            }

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

        repeat(10) {
            emailTextField.performTextInput(" ")
            rule.onNodeWithText(SignInEmail)
        }

        repeat(10) {
            passwordTextField.performTextInput(" ")
            rule.onNodeWithText(getInvisibleText(SignInPassword.length))
        }

        repeat(10) {
            passwordConfirmTextField.performTextInput(" ")
            rule.onNodeWithText(getInvisibleText(SignInPassword.length))
        }
    }

    @Test
    fun signUpNameBlankTest() { // with SignUp
        emailTextField.performTextInput(SignUpEmail)
        passwordTextField.performTextInput(SignUpEmail)
        passwordConfirmTextField.performTextInput(SignUpEmail)

        nameTextField.performTextInput("  $SignUpName  ")

        signUpButton.performClick()

        rule.waitTagExist(MainScreenTag)
        runBlocking { delay(5000) }
        assert(Firebase.auth.currentUser?.displayName == SignUpName)

        Firebase.auth.currentUser?.delete()
    }
}