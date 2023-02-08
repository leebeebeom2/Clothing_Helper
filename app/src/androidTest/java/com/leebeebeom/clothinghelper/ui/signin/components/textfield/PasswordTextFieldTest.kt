package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.layout.Column
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.CustomTestRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.R.string.dummy
import com.leebeebeom.clothinghelper.activityRule
import com.leebeebeom.clothinghelper.passwordTextField
import com.leebeebeom.clothinghelper.ui.components.*
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailAndPasswordUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInViewModel
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PasswordTextFieldTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule = rule)
    private lateinit var viewModel: SignInViewModel
    private lateinit var uiState: MutableEmailAndPasswordUiState

    private val passwordTextField get() = passwordTextField(customTestRule)
    private val dummyButton get() = customTestRule.getNodeWithStringRes(dummy)

    @Before
    fun init() {
        customTestRule.setContent {
            ClothingHelperTheme {
                viewModel = hiltViewModel()
                uiState = viewModel.uiState as MutableEmailAndPasswordUiState
                Column {
                    PasswordTextField(
                        error = { uiState.passwordError },
                        onInputChange = viewModel::onPasswordChange,
                        state = rememberPasswordTextFieldState(imeActionRoute = ImeActionRoute.DONE)
                    )
                    MaxWidthButton(text = dummy) {}
                }

            }
        }
    }

    @Test
    fun showKeyboardTest() = showKeyboardTest(
        textField = passwordTextField,
        showKeyboard = false
    )

    @Test
    fun inputChangeTest() =
        invisibleTest(customTestRule) {
            inputChangeTest(
                rule = customTestRule,
                textField = { passwordTextField },
                input = { uiState.password },
                invisible = it
            )
        }

    @Test
    fun textFieldVisibleTest() = textFieldVisibleTest(
        rule = customTestRule,
        textField = { passwordTextField },
        input = { uiState.password }
    )

    @Test
    fun errorTest() =
        invisibleTest(customTestRule) {
            errorTest(
                rule = customTestRule,
                errorTextField = { passwordTextField },
                errorTextRes = R.string.error_test,
                setError = { uiState.passwordError = R.string.error_test },
                blockBlank = true,
                invisible = it
            )
        }

    @Test
    fun cursorTest() =
        invisibleTest(customTestRule) {
            cursorTest(
                rule = customTestRule,
                textField = { passwordTextField },
                looseFocus = { dummyButton.click() },
                invisible = it
            )
        }

    @Test
    fun blockBlankTest() = invisibleTest(customTestRule) {
        blockBlankTest(
            rule = customTestRule,
            textField = { passwordTextField },
            invisible = it
        )
    }
}