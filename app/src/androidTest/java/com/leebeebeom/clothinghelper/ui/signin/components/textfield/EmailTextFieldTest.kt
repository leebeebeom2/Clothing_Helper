package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.R.string.check
import com.leebeebeom.clothinghelper.R.string.error_test
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute
import com.leebeebeom.clothinghelper.ui.components.MaxWidthTextFieldWithError
import com.leebeebeom.clothinghelper.ui.components.rememberMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailUiState
import com.leebeebeom.clothinghelper.ui.signin.ui.signin.SignInViewModel
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EmailTextFieldTest {
    @get:Rule
    val rule = activityRule
    private val restoreTester = restoreTester(rule)
    private lateinit var viewmodel: SignInViewModel
    private lateinit var uiState: MutableEmailUiState
    private val ComposeContentTestRule.dummyTextField get() = onNodeWithText("확인")

    @Before
    fun init() {
        restoreTester.setContent {
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
                    MaxWidthTextFieldWithError(
                        state = state2,
                        onValueChange = state2::onValueChange,
                        onFocusChanged = state2::onFocusChanged,
                        onInputChange = {}
                    )
                }
            }
        }
    }

    @Test
    fun showKeyboardTest() = isKeyboardShown()

    @Test
    fun inputChangeTest() =
        inputChangeTest(
            textField = rule.emailTextField,
            rule = rule,
            input = { uiState.email },
            restoreTester = restoreTester
        )

    @Test
    fun cancelIconTest() =
        cancelIconTest(
            cancelIconTestField = rule.emailTextField,
            noCancelIconTestField = rule.dummyTextField,
            rule = rule,
            cancelIconTextFieldInput = { uiState.email },
            restorationTester = restoreTester
        )

    @Test
    fun errorTest() =
        errorTest(
            rule = rule,
            errorTextField = rule.emailTextField,
            setError = { uiState.emailError = error_test },
            restoreTester = restoreTester,
            blockBlank = true
        )

    @Test
    fun blockBlankTest() = blockBlankTest(
        rule = rule,
        textField = rule.emailTextField,
        restoreTester = restoreTester
    )

    @Test
    fun cursorTest() {
        cursorTest(
            restoreTester = restoreTester,
            textField1 = rule.emailTextField,
            textField2 = rule.dummyTextField
        )
        cursorTest(
            restoreTester = restoreTester,
            textField1 = rule.dummyTextField,
            textField2 = rule.emailTextField
        )
    }

    @Test
    fun imeTest() = imeTest(
        rule.emailTextField,
        doneTextField = rule.dummyTextField
    )
}