package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.layout.Column
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
    
    private val passwordTextField get() = passwordTextField(customTestRule)
    private val invisibleIcon get() = invisibleIcon(customTestRule)
    private val dummyTextField get() = customTestRule.getNodeWithStringRes(R.string.check)

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
                textField = { passwordTextField },
                input = { uiState.password },
                invisible = it
            )
        }

    @Test
    fun textFieldVisibleTest() = textFieldVisibleTest(
        rule = customTestRule,
        textField = { passwordTextField }
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
    fun cursorTest() {
        invisibleTest(customTestRule) {
            cursorTest(
                rule = customTestRule,
                textField1 = { passwordTextField },
                textField2 = { dummyTextField },
                invisible = it
            )
        }

        invisibleIcon.click()

        invisibleTest(customTestRule) {
            cursorTest(
                rule = customTestRule,
                textField1 = { dummyTextField },
                textField2 = { passwordTextField }
            )
        }
    }

    @Test
    fun imeTest() = imeTest({ dummyTextField },
        doneTextField = { passwordTextField })

    @Test
    fun blockBlankTest() {
        visibleIcon(customTestRule).click()

        blockBlankTest(
            rule = customTestRule,
            textField = { passwordTextField }
        )
    }
}