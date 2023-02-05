package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.*
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

    @Before
    fun init() {
        customTestRule.setContent {
            ClothingHelperTheme {
                viewModel = hiltViewModel()
                uiState = viewModel.uiState as MutableEmailAndPasswordUiState

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
                        state = rememberPasswordTextFieldState(imeActionRoute = ImeActionRoute.DONE)
                    )
                }

            }
        }
    }

    @Test
    fun showKeyboardTest() = isKeyboardNotShown()

    @Test
    fun inputChangeTest() =
        invisibleTest(customTestRule) {
            inputChangeTest(
                rule = customTestRule,
                textField = { customTestRule.passwordTextField },
                input = { uiState.password },
                invisible = it
            )
        }

    @Test
    fun textFieldVisibleTest() = textFieldVisibleTest(
        rule = customTestRule,
        textField = { customTestRule.passwordTextField }
    )

    @Test
    fun errorTest() =
        invisibleTest(customTestRule) {
            errorTest(
                customTestRule,
                { customTestRule.passwordTextField },
                rule.activity.getString(R.string.error_test),
                { uiState.passwordError = R.string.error_test },
                blockBlank = true,
                invisible = it
            )
        }

    @Test
    fun cursorTest() {
        invisibleTest(customTestRule) {
            cursorTest(
                rule = customTestRule,
                textField1 = { customTestRule.passwordTextField },
                textField2 = { customTestRule.dummyTextField },
                invisible = it
            )
        }

        customTestRule.invisibleIcon.click()

        invisibleTest(customTestRule) {
            cursorTest(
                rule = customTestRule,
                textField1 = { customTestRule.dummyTextField },
                textField2 = { customTestRule.passwordTextField }
            )
        }
    }

    @Test
    fun imeTest() = imeTest({ customTestRule.dummyTextField },
        doneTextField = { customTestRule.passwordTextField })

    @Test
    fun blockBlankTest() {
        rule.visibleIcon.performClick()

        blockBlankTest(
            rule = customTestRule,
            textField = { customTestRule.passwordTextField }
        )
    }
}