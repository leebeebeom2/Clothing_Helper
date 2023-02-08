package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.R.string.error_password_confirm_not_same
import com.leebeebeom.clothinghelper.ui.components.*
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)
    private lateinit var uiState: MutableSignUpUiState

    private val emailTextField get() = emailTextField(customTestRule)
    private val nickNameTextField get() = nickNameTextField(customTestRule)
    private val passwordTextField get() = passwordTextField(customTestRule)
    private val passwordConfirmTextField get() = passwordConfirmTextField(customTestRule)
    private val passwordVisibleIcon
        get() = customTestRule.getNodeWithDescription(PASSWORD_VISIBLE_ICON)
    private val passwordConfirmVisibleIcon
        get() = customTestRule.getNodeWithDescription(PASSWORD_CONFIRM_VISIBLE_ICON)
    private val passwordInvisibleIcon
        get() = customTestRule.getNodeWithDescription(PASSWORD_INVISIBLE_ICON)
    private val passwordConfirmInvisibleIcon
        get() = customTestRule.getNodeWithDescription(PASSWORD_CONFIRM_INVISIBLE_ICON)
    private val signUpButton get() = signUpButton(customTestRule)

    @Before
    fun init() {
        customTestRule.setContent {
            ClothingHelperTheme {
                val navViewModel: SignInNavViewModel = hiltViewModel()
                val viewModel: SignUpViewModel = hiltViewModel()
                uiState = viewModel.uiState as MutableSignUpUiState

                SignUpScreen(
                    signInNavViewModel = navViewModel,
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
    }

    @Test
    fun showKeyboardTest() = showKeyboardTest(emailTextField)

    @Test
    fun inputChangeTest() {
        inputChangeTest(
            rule = customTestRule,
            textField = { emailTextField },
            input = { uiState.email })
        inputChangeTest(
            rule = customTestRule,
            textField = { nickNameTextField },
            input = { uiState.nickName })

        invisibleTest(rule = customTestRule, visibleIcon = { passwordVisibleIcon }) {
            inputChangeTest(
                rule = customTestRule,
                textField = { passwordTextField },
                input = { uiState.password },
                invisible = it
            )
        }

        invisibleTest(rule = customTestRule, visibleIcon = { passwordConfirmVisibleIcon }) {
            inputChangeTest(
                rule = customTestRule,
                textField = { passwordConfirmTextField },
                input = { uiState.passwordConfirm },
                invisible = it
            )
        }
    }

    @Test
    fun passwordTextFieldVisibleTest() {
        textFieldVisibleTest(
            rule = customTestRule,
            textField = { passwordTextField },
            input = { uiState.password },
            visibleIcon = { passwordVisibleIcon },
            invisibleIcon = { passwordInvisibleIcon }
        )

        textFieldVisibleTest(
            rule = customTestRule,
            textField = { passwordConfirmTextField },
            input = { uiState.passwordConfirm },
            visibleIcon = { passwordConfirmVisibleIcon },
            invisibleIcon = { passwordConfirmInvisibleIcon }
        )
    }

    @Test
    fun cancelIconTest() {
        cancelIconTest(
            rule = customTestRule,
            cancelIconTextField = { emailTextField },
            looseFocus = { passwordTextField.click() }
        )

        cancelIconTest(
            rule = customTestRule,
            cancelIconTextField = { nickNameTextField },
            looseFocus = { passwordTextField.click() }
        )

        invisibleTest(rule = customTestRule, visibleIcon = { passwordVisibleIcon }) {
            cancelIconTest(
                rule = customTestRule,
                cancelIconTextField = { passwordTextField },
                looseFocus = { passwordConfirmTextField.click() },
                enable = false,
                invisible = it
            )
        }
        invisibleTest(rule = customTestRule, visibleIcon = { passwordConfirmVisibleIcon }) {
            cancelIconTest(
                rule = customTestRule,
                cancelIconTextField = { passwordConfirmTextField },
                looseFocus = { passwordTextField.click() },
                enable = false,
                invisible = it
            )
        }
    }

    @Test
    fun errorTest() {
        errorTest(
            rule = customTestRule,
            errorTextField = { emailTextField },
            errorTextRes = R.string.error_email_already_in_use,
            setError = {
                allTextFieldInput(
                    email = EMAIL,
                    password = PASSWORD,
                    passwordConfirm = PASSWORD
                )
                signUpButton.click()
            },
            beforeText = EMAIL,
            blockBlank = true,
            clearOtherTextField = ::allTextFieldClear
        )
        errorTest(
            rule = customTestRule,
            errorTextField = { emailTextField },
            errorTextRes = R.string.error_invalid_email,
            setError = {
                allTextFieldInput(
                    email = INVALID_EMAIL,
                    password = PASSWORD,
                    passwordConfirm = PASSWORD
                )
                signUpButton.click()
            },
            beforeText = INVALID_EMAIL,
            blockBlank = true,
            clearOtherTextField = ::allTextFieldClear
        )

        weakPasswordErrorTest()
        allTextFieldClear()
        passwordNotSameErrorTest1()
        allTextFieldClear()
        passwordNotSameErrorTest2()
    }

    private fun allTextFieldClear() {
        emailTextField.textClear()
        nickNameTextField.textClear()
        passwordTextField.textClear()
        passwordConfirmTextField.textClear()
    }

    private fun weakPasswordErrorTest() {
        fun weakPasswordError() =
            customTestRule.getNodeWithStringRes(R.string.error_weak_password)

        fun errorExist() = weakPasswordError().exist()
        fun errorNotExist() = weakPasswordError().notExist()

        allTextFieldInput(
            email = NOT_EXIST_EMAIL,
            password = WEAK_PASSWORD
        )

        errorExist()

        repeat(4) {
            passwordTextField.inputWithNoRestore(it + 1, WEAK_PASSWORD)
            errorExist()
        }

        passwordTextField.inputWithNoRestore(5, WEAK_PASSWORD)
        errorNotExist()

        repeat(5) {
            passwordTextField.delete()
            errorExist()
        }

        passwordTextField.delete()
        errorNotExist()
    }

    private fun passwordNotSameError() =
        customTestRule.getNodeWithStringRes(error_password_confirm_not_same)

    private fun passwordNotSameErrorExist() = passwordNotSameError().exist()
    private fun passwordNotSameErrorNotExist() = passwordNotSameError().notExist()

    /**
     * 순수 passwordConfirmError
     */
    private fun passwordNotSameErrorTest1() {

        allTextFieldInput(
            email = EMAIL,
            password = PASSWORD,
            passwordConfirm = ""
        )

        passwordNotSameErrorNotExist()
        repeat(5) {
            passwordConfirmTextField.input("1", beforeText = "1".repeat(it), invisible = true)
            passwordNotSameErrorExist()
        }
        passwordConfirmTextField.input("1", "11111", invisible = true)
        passwordNotSameErrorNotExist()

        passwordConfirmTextField.input("1", "111111", invisible = true)
        passwordNotSameErrorExist()

        passwordConfirmTextField.delete()
        passwordNotSameErrorNotExist()
    }

    /**
     * passwordTextField와 연동된 에러
     */
    private fun passwordNotSameErrorTest2() {
        allTextFieldInput(
            email = EMAIL,
            password = PASSWORD,
            passwordConfirm = PASSWORD
        )

        passwordTextField.input(text = "1", beforeText = PASSWORD, invisible = true)
        passwordNotSameErrorExist()

        passwordTextField.delete()
        passwordNotSameErrorNotExist()

        passwordTextField.delete()
        passwordNotSameErrorExist()
    }

    private fun allTextFieldInput(
        email: String,
        password: String,
        passwordConfirm: String = "",
    ) {
        emailTextField.input(email)
        nickNameTextField.input(NICK_NAME)
        passwordTextField.input(password, invisible = true)
        passwordConfirmTextField.input(passwordConfirm, invisible = true)
    }

    @Test
    fun cursorTest() {
        cursorTest(
            rule = customTestRule,
            textField = { emailTextField },
            looseFocus = { passwordTextField.click() }
        )
        cursorTest(
            rule = customTestRule,
            textField = { nickNameTextField },
            looseFocus = { passwordTextField.click() }
        )
        invisibleTest(rule = customTestRule, visibleIcon = { passwordVisibleIcon }) {
            cursorTest(
                rule = customTestRule,
                textField = { passwordTextField },
                looseFocus = { passwordConfirmTextField.click() },
                invisible = it
            )
        }
        invisibleTest(customTestRule, { passwordConfirmVisibleIcon }) {
            cursorTest(
                rule = customTestRule,
                textField = { passwordConfirmTextField },
                looseFocus = { passwordTextField.click() },
                invisible = it
            )
        }
    }

    @Test
    fun imeTest() {
        imeTest(
            { emailTextField },
            { nickNameTextField },
            { passwordTextField },
            doneTextField = { passwordConfirmTextField }
        )
        imeTest(
            { nickNameTextField },
            { passwordTextField },
            doneTextField = { passwordConfirmTextField }
        )
        imeTest(
            { passwordTextField },
            doneTextField = { passwordConfirmTextField }
        )
    }

    @Test
    fun blockBlankTest() {
//        com.leebeebeom.clothinghelper.ui.components.blockBlankTest(
//            customTestRule,
//
//        )
    }
}