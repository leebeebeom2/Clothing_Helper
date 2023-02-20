package com.leebeebeom.clothinghelper.ui.signin.ui.signin

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.CustomTestRule.CustomSemanticsNodeInteraction
import com.leebeebeom.clothinghelper.R.string.*
import com.leebeebeom.clothinghelper.ui.ActivityDestinations.SIGN_IN_ROUTE
import com.leebeebeom.clothinghelper.ui.components.*
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailAndPasswordUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// TODO 구글 로그인 테스트

class SignInScreenTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)
    private lateinit var viewModel: SignInViewModel
    private lateinit var uiState: MutableEmailAndPasswordUiState

    private val emailTextField get() = emailTextField(customTestRule)
    private val passwordTextField get() = passwordTextField(customTestRule)
    private val signInButton get() = signInButton(customTestRule)

    @Before
    fun init() {
        customTestRule.setContent {
            val signInGraphViewModel: SignInNavViewModel = hiltViewModel()
            ClothingHelperTheme {
                NavHost(
                    navController = rememberNavController(),
                    startDestination = SIGN_IN_ROUTE,
                ) {
                    composable(SIGN_IN_ROUTE) {
                        viewModel = hiltViewModel()
                        uiState = viewModel.uiState as MutableEmailAndPasswordUiState
                        SignInScreen(
                            viewModel = viewModel,
                            navigateToResetPassword = { },
                            navigateToSignUp = { },
                            signInNavViewModel = signInGraphViewModel
                        )
                    }
                }
            }
        }
    }

    @Test
    fun showKeyboardTest() = showKeyboardTest(textField = emailTextField)

    @Test
    fun inputChangeTest() {
        inputChangeTest(rule = customTestRule,
            textField = { emailTextField },
            input = { uiState.email })

        invisibleTest(customTestRule) {
            inputChangeTest(
                rule = customTestRule,
                textField = { passwordTextField },
                input = { uiState.password },
                invisible = it
            )
        }
    }

    @Test
    fun textFieldVisibleTest() = textFieldVisibleTest(rule = customTestRule,
        textField = { passwordTextField },
        input = { uiState.password })

    @Test
    fun cancelIconTest() {
        cancelIconTest(rule = customTestRule,
            cancelIconTextField = { emailTextField },
            looseFocus = { passwordTextField.click() })

        invisibleTest(customTestRule) {
            cancelIconTest(
                rule = customTestRule,
                cancelIconTextField = { passwordTextField },
                looseFocus = { emailTextField.click() },
                enable = false,
                invisible = it
            )
        }
    }

    @Test
    fun blockBlankTest() {
        blockBlankTest(rule = customTestRule, textField = { emailTextField })
        invisibleTest(customTestRule) {
            blockBlankTest(
                rule = customTestRule, textField = { passwordTextField }, invisible = it
            )
        }
    }

    @Test
    fun errorTest() {
        fun allFieldClear() {
            emailTextField.textClear()
            passwordTextField.textClear()
        }

        fun clearPassword() = passwordTextField.textClear(
            beforeText = customTestRule.getInvisibleText(
                PASSWORD
            )
        )

        errorTest(
            rule = customTestRule,
            errorTextField = { emailTextField },
            errorTextRes = error_invalid_email,
            setError = {
                emailTextField.input(text = INVALID_EMAIL)
                passwordTextField.input(text = PASSWORD, invisible = true)
                signInButton.click()
            },
            blockBlank = true,
            beforeText = INVALID_EMAIL,
            clearOtherTextField = ::clearPassword
        )

        allFieldClear()

        errorTest(
            rule = customTestRule,
            errorTextField = { emailTextField },
            errorTextRes = error_user_not_found,
            setError = {
                emailTextField.input(text = NOT_EXIST_EMAIL)
                passwordTextField.input(text = PASSWORD, invisible = true)
                signInButton.click()
            },
            blockBlank = true,
            beforeText = NOT_EXIST_EMAIL,
            clearOtherTextField = ::clearPassword
        )

        allFieldClear()

        errorTest(
            rule = customTestRule,
            errorTextField = { passwordTextField },
            errorTextRes = error_wrong_password,
            setError = {
                emailTextField.input(text = EMAIL)
                passwordTextField.input(text = WRONG_PASSWORD, invisible = true)
                signInButton.click()
            },
            blockBlank = true,
            beforeText = customTestRule.getInvisibleText(WRONG_PASSWORD),
            clearOtherTextField = { emailTextField.textClear(EMAIL) },
            invisible = true
        )
    }

    @Test
    fun cursorTest() {
        cursorTest(rule = customTestRule,
            textField = { emailTextField },
            looseFocus = { passwordTextField.click() })

        invisibleTest(customTestRule) {
            cursorTest(
                rule = customTestRule,
                textField = { passwordTextField },
                looseFocus = { emailTextField.click() },
                invisible = it
            )
        }
    }

    @Test
    fun imeTest() = imeTest({ emailTextField }, doneTextField = { passwordTextField })

    @Test
    fun buttonEnabledTest() {
        fun buttonNotEnabled() = signInButton.notEnabled()
        fun buttonEnabled() = signInButton.enabled()
        fun oneTextFieldBlank(
            inputTextFiled: () -> CustomSemanticsNodeInteraction,
            text: String,
            invisible: Boolean = false,
        ) {
            inputTextFiled().input(text = text, invisible = invisible)
            buttonNotEnabled()
            inputTextFiled().textClear(beforeText = text, invisible = invisible)
            buttonNotEnabled()
        }

        buttonNotEnabled()

        oneTextFieldBlank(inputTextFiled = { emailTextField }, text = EMAIL)
        invisibleTest(customTestRule) {
            oneTextFieldBlank(
                inputTextFiled = { passwordTextField }, text = PASSWORD, invisible = it
            )
        }

        emailTextField.input(INVALID_EMAIL)
        passwordTextField.input(PASSWORD)
        buttonEnabled()

        signInButton.click() // invalid email error
        customTestRule.waitTextExist(customTestRule.getString(error_invalid_email))
        buttonNotEnabled()
        emailTextField.input("1", INVALID_EMAIL) // remove invalid email error
        buttonEnabled()

        emailTextField.textReplace(NOT_EXIST_EMAIL)
        signInButton.click() // user not found error
        customTestRule.waitTextExist(customTestRule.getString(error_user_not_found))
        buttonNotEnabled()
        emailTextField.input("1", NOT_EXIST_EMAIL) // remove user not found error
        buttonEnabled()

        emailTextField.textReplace(EMAIL)
        passwordTextField.textReplace(WRONG_PASSWORD)
        signInButton.click() // wrong password error
        customTestRule.waitTextExist(customTestRule.getString(error_wrong_password))
        buttonNotEnabled()
        passwordTextField.input("1", WRONG_PASSWORD) // wrong password error
        buttonEnabled()
    }

    @Test
    fun signInTest() {
        emailTextField.input(EMAIL)
        passwordTextField.input(text = PASSWORD, invisible = true)
        signInButton.click()

        rule.waitUntil { FirebaseAuth.getInstance().currentUser != null }

        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun looseFocusTest() {
        looseFocusTest(
            textField = { emailTextField },
        ) { customTestRule.getNodeWithTag(SIGN_IN_SCREEN_TAG) }

        looseFocusTest(
            textField = { passwordTextField },
        ) { customTestRule.getNodeWithTag(SIGN_IN_SCREEN_TAG) }
    }
}

fun looseFocusTest(
    textField: () -> CustomSemanticsNodeInteraction,
    root: () -> CustomSemanticsNodeInteraction,
) {
    textField().click()
    textField().focused()
    root().click()
    textField().notFocused()
}