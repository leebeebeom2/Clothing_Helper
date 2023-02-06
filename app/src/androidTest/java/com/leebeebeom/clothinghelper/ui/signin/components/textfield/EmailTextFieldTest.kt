package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.CustomTestRule
import com.leebeebeom.clothinghelper.R.string.check
import com.leebeebeom.clothinghelper.R.string.error_test
import com.leebeebeom.clothinghelper.activityRule
import com.leebeebeom.clothinghelper.ui.components.*
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInViewModel
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EmailTextFieldTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)
    private lateinit var viewmodel: SignInViewModel
    private lateinit var uiState: MutableEmailUiState

    @Before
    fun init() {
        customTestRule.setContent {
            ClothingHelperTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    viewmodel = hiltViewModel()
                    uiState = viewmodel.uiState as MutableEmailUiState
                    EmailTextField(
                        state = rememberEmailTextFieldState(imeActionRoute = ImeActionRoute.NEXT),
                        error = { uiState.emailError },
                        onInputChange = viewmodel::onEmailChange,
                        imeActionRoute = ImeActionRoute.DONE
                    )

                    val state2 = rememberMaxWidthTextFieldState(label = check)
                    MaxWidthTextFieldWithError(state = state2,
                        onValueChange = state2::onValueChange,
                        onFocusChanged = state2::onFocusChanged,
                        onInputChange = {})
                }
            }
        }
    }

    @Test
    fun showKeyboardTest() = showKeyboardTest(textField = customTestRule.emailTextField)

    @Test
    fun inputChangeTest() = inputChangeTest(
        rule = customTestRule,
        textField = { customTestRule.emailTextField },
        input = { uiState.email }
    )

    @Test
    fun cancelIconTest() = cancelIconTest(
        rule = customTestRule,
        cancelIconTextField = { customTestRule.emailTextField },
        noCancelIconTextField = { customTestRule.dummyTextField }
    )

    @Test
    fun errorTest() = errorTest(
        rule = customTestRule,
        errorTextField = { customTestRule.emailTextField },
        errorTextRes = error_test,
        setError = { uiState.emailError = error_test }
    )

    @Test
    fun blockBlankTest() = blockBlankTest(
        rule = customTestRule,
        textField = { customTestRule.emailTextField }
    )

    @Test
    fun cursorTest() {
        cursorTest(
            rule = customTestRule,
            textField1 = { customTestRule.emailTextField },
            textField2 = { customTestRule.dummyTextField }
        )
        cursorTest(
            rule = customTestRule,
            textField1 = { customTestRule.dummyTextField },
            textField2 = { customTestRule.emailTextField }
        )
    }

    @Test
    fun imeTest() = imeTest(
        { customTestRule.emailTextField },
        doneTextField = { customTestRule.dummyTextField },
    )
}