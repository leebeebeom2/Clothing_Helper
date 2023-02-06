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

class SignInScreenTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)
    private lateinit var viewModel: SignInViewModel
    private lateinit var uiState: MutableEmailAndPasswordUiState

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
    fun showKeyboardTest() = showKeyboardTest(textField = customTestRule.emailTextField)

    @Test
    fun inputChangeTest() {
        inputChangeTest(rule = customTestRule,
            textField = { customTestRule.emailTextField },
            input = { uiState.email })
        invisibleTest(customTestRule) {
            inputChangeTest(
                rule = customTestRule,
                textField = { customTestRule.passwordTextField },
                input = { uiState.password },
                invisible = it
            )
        }
    }

    @Test
    fun textFieldVisibleTest() = textFieldVisibleTest(rule = customTestRule,
        textField = { customTestRule.passwordTextField })

    @Test
    fun cancelIconTest() = cancelIconTest(rule = customTestRule,
        cancelIconTextField = { customTestRule.emailTextField },
        noCancelIconTextField = { customTestRule.passwordTextField })

    @Test
    fun blockBlankTest() {
        blockBlankTest(rule = customTestRule, textField = { customTestRule.emailTextField })
        invisibleTest(customTestRule) {
            blockBlankTest(
                rule = customTestRule,
                textField = { customTestRule.passwordTextField },
                invisible = it
            )
        }
    }

    @Test
    fun errorTest() {
        fun allFieldClear() {
            customTestRule.emailTextField.textClear()
            customTestRule.passwordTextField.textClear()
        }

        fun clearPassword() = customTestRule.passwordTextField.textClear(
            beforeText = customTestRule.getInvisibleText(
                PASSWORD
            )
        )

        errorTest(
            rule = customTestRule,
            errorTextField = { customTestRule.emailTextField },
            errorTextRes = error_invalid_email,
            setError = {
                customTestRule.emailTextField.input(INVALID_EMAIL)
                customTestRule.passwordTextField.invisibleInput(PASSWORD)
                customTestRule.signInButton.click()
            },
            blockBlank = true,
            beforeText = INVALID_EMAIL,
            clearOtherTextField = ::clearPassword
        )

        allFieldClear()

        errorTest(
            rule = customTestRule,
            errorTextField = { customTestRule.emailTextField },
            errorTextRes = error_user_not_found,
            setError = {
                customTestRule.emailTextField.input(NOT_EXIST_EMAIL)
                customTestRule.passwordTextField.invisibleInput(PASSWORD)
                customTestRule.signInButton.click()
            },
            blockBlank = true,
            beforeText = NOT_EXIST_EMAIL,
            clearOtherTextField = ::clearPassword
        )

        allFieldClear()

        errorTest(
            rule = customTestRule,
            errorTextField = { customTestRule.passwordTextField },
            errorTextRes = error_wrong_password,
            setError = {
                customTestRule.emailTextField.input(EMAIL)
                customTestRule.passwordTextField.invisibleInput(WRONG_PASSWORD)
                customTestRule.signInButton.click()
            },
            blockBlank = true,
            beforeText = customTestRule.getInvisibleText(WRONG_PASSWORD),
            clearOtherTextField = { customTestRule.emailTextField.textClear(EMAIL) },
            invisible = true
        )

        allFieldClear()
        customTestRule.visibleIcon.click()

        errorTest(rule = customTestRule,
            errorTextField = { customTestRule.passwordTextField },
            errorTextRes = error_wrong_password,
            setError = {
                customTestRule.emailTextField.input(EMAIL)
                customTestRule.passwordTextField.input(WRONG_PASSWORD)
                customTestRule.signInButton.click()
            },
            blockBlank = true,
            beforeText = WRONG_PASSWORD,
            clearOtherTextField = { customTestRule.emailTextField.textClear(EMAIL) })
    }

    @Test
    fun cursorTest() {
        cursorTest(rule = customTestRule,
            textField1 = { customTestRule.emailTextField },
            textField2 = { customTestRule.passwordTextField })

        invisibleTest(customTestRule) {
            cursorTest(
                rule = customTestRule,
                textField1 = { customTestRule.passwordTextField },
                textField2 = { customTestRule.emailTextField },
                invisible = it
            )
        }
    }

    @Test
    fun imeTest() = imeTest({ customTestRule.emailTextField },
        doneTextField = { customTestRule.passwordTextField })

    @Test
    fun buttonEnabledTest() {
        fun buttonNotEnabled() = customTestRule.signInButton.notEnabled()
        fun buttonEnabled() = customTestRule.signInButton.enabled()
        fun oneTextFieldBlank(
            inputTextFiled: () -> CustomSemanticsNodeInteraction,
            text: String,
            invisible: Boolean = false
        ) {
            if (invisible) inputTextFiled().invisibleInput(text)
            else inputTextFiled().input(text)
            buttonNotEnabled()
            inputTextFiled().textClear(if (invisible) customTestRule.getInvisibleText(text) else text)
            buttonNotEnabled()
        }

        buttonNotEnabled()

        oneTextFieldBlank(inputTextFiled = { customTestRule.emailTextField }, text = EMAIL)
        invisibleTest(customTestRule) {
            oneTextFieldBlank(
                inputTextFiled = { customTestRule.passwordTextField }, text = PASSWORD,
                invisible = it
            )
        }

        customTestRule.emailTextField.input(INVALID_EMAIL)
        customTestRule.passwordTextField.input(PASSWORD)
        buttonEnabled()

        customTestRule.signInButton.click() // invalid email error
        customTestRule.waitTextExist(customTestRule.getString(error_invalid_email))
        buttonNotEnabled()
        customTestRule.emailTextField.input("1", INVALID_EMAIL) // remove invalid email error
        buttonEnabled()

        customTestRule.emailTextField.textReplace(NOT_EXIST_EMAIL)
        customTestRule.signInButton.click() // user not found error
        customTestRule.waitTextExist(customTestRule.getString(error_user_not_found))
        buttonNotEnabled()
        customTestRule.emailTextField.input("1", NOT_EXIST_EMAIL) // remove user not found error
        buttonEnabled()

        customTestRule.emailTextField.textReplace(EMAIL)
        customTestRule.passwordTextField.textReplace(WRONG_PASSWORD)
        customTestRule.signInButton.click() // wrong password error
        customTestRule.waitTextExist(customTestRule.getString(error_wrong_password))
        buttonNotEnabled()
        customTestRule.passwordTextField.input("1", WRONG_PASSWORD) // wrong password error
        buttonEnabled()
    }

    @Test
    fun signInTest() {
        fun innerSignInTest(invisible: Boolean = false) {
            customTestRule.emailTextField.input(EMAIL)
            if (invisible) customTestRule.passwordTextField.invisibleInput(PASSWORD)
            else customTestRule.passwordTextField.input(PASSWORD)
            customTestRule.signInButton.click()

            rule.waitUntil { FirebaseAuth.getInstance().currentUser != null }

            FirebaseAuth.getInstance().signOut()
        }


        customTestRule.emailTextField.textClear(EMAIL)
        customTestRule.passwordTextField.textClear(customTestRule.getInvisibleText(PASSWORD))

        customTestRule.visibleIcon.click()

        innerSignInTest(invisible = false)
    }

    @Test
    fun looseFocusTest() {
        looseFocusTest(
            rule = customTestRule,
            textField = { customTestRule.emailTextField },
            rootTag = SIGN_IN_SCREEN_TAG,
        )

        looseFocusTest(
            rule = customTestRule,
            textField = { customTestRule.passwordTextField },
            rootTag = SIGN_IN_SCREEN_TAG,
        )
    }
}

fun looseFocusTest(
    rule: CustomTestRule,
    textField: () -> CustomSemanticsNodeInteraction,
    rootTag: String
) {
    textField().click()
    textField().focused()
    rule.getNodeWithTag(rootTag).click()
    textField().notFocused()
}