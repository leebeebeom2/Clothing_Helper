package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute
import com.leebeebeom.clothinghelper.ui.components.MaxWidthTextFieldWithError
import com.leebeebeom.clothinghelper.ui.components.MutableMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.components.rememberMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailAndPasswordUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInViewModel
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PasswordTextFieldTest {
    @get:Rule
    val rule = activityRule
    private val restoreTester = restoreTester(rule)
    private lateinit var viewModel: SignInViewModel
    private lateinit var uiState: MutableEmailAndPasswordUiState
    private lateinit var state: MutableMaxWidthTextFieldState
    private val ComposeContentTestRule.dummyTextField get() = onNodeWithText("확인")

    @Before
    fun init() {
        restoreTester.setContent {
            ClothingHelperTheme {
                viewModel = hiltViewModel()
                uiState = viewModel.uiState as MutableEmailAndPasswordUiState
                state = rememberPasswordTextFieldState(imeActionRoute = ImeActionRoute.DONE)

                val state2 = rememberMaxWidthTextFieldState(
                    label = R.string.check,
                    imeActionRoute = ImeActionRoute.NEXT
                )

                Column {
                    MaxWidthTextFieldWithError(
                        state = state2,
                        onValueChange = state2::onValueChange,
                        onFocusChanged = state2::onFocusChanged,
                        onInputChange = {}
                    )
                    PasswordTextField(
                        error = { uiState.passwordError },
                        onInputChange = viewModel::onPasswordChange,
                        state = state
                    )
                }

            }
        }
    }

    @Test
    fun showKeyboardTest() = isKeyboardNotShown()

    @Test
    fun inputChangeTest() {
        rule.visibleIcon.performClick()

        inputChangeTest(
            textField = rule.passwordTextField,
            rule = rule,
            input = { uiState.password },
            restoreTester = restoreTester,
            state = { state }
        )
    }

    @Test
    fun textFieldVisibleTest() = textFieldVisibleTest(
        rule = rule,
        input = { uiState.password },
        restoreTester = restoreTester
    )

    @Test
    fun errorTest() = errorTest(
        rule = rule,
        errorTextField = rule.passwordTextField,
        setError = { uiState.passwordError = R.string.error_test },
        restoreTester = restoreTester,
        blockBlank = true
    )

    @Test
    fun cursorTest() {
        rule.visibleIcon.performClick()

        cursorTest(
            restoreTester,
            textField1 = rule.passwordTextField,
            textField2 = rule.dummyTextField
        )
        cursorTest(
            restoreTester,
            textField1 = rule.dummyTextField,
            textField2 = rule.passwordTextField
        )
    }

    @Test
    fun imeTest() = imeTest(rule.dummyTextField, doneTextField = rule.passwordTextField)

    @Test
    fun blockBlankTest() {
        rule.visibleIcon.performClick()

        blockBlankTest(
            rule = rule,
            textField = rule.passwordTextField,
            restoreTester = restoreTester
        )
    }
}