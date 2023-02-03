package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.R.string.*
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.VisibleIcon
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val MOVE_BUTTON_TEXT = "이동"
private const val MOVE_BACK_BUTTON_TEXT = "뒤로 이동"
private const val SCREEN_1_ROUTE = "screen1"
private const val SCREEN_2_ROUTE = "screen2"

class MaxWidthTextFieldTest {
    private lateinit var emailInput: String
    private lateinit var passwordInput: String
    private lateinit var state: MutableMaxWidthTextFieldState
    private lateinit var passwordState: MutableMaxWidthTextFieldState
    private val moveButton get() = rule.onNodeWithText(MOVE_BUTTON_TEXT)
    private val moveBackButton get() = rule.onNodeWithText(MOVE_BACK_BUTTON_TEXT)

    @get:Rule
    val rule = composeRule
    private val restoreTester = restoreTester(rule)

    @Before
    fun init() {
        emailInput = ""
        passwordInput = ""

        restoreTester.setContent {
            ClothingHelperTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = SCREEN_1_ROUTE) {
                    composable(SCREEN_1_ROUTE) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            emailTextField()()
                            passwordTextField()()

                            Button(onClick = { navController.navigate(SCREEN_2_ROUTE) }) {
                                Text(text = MOVE_BUTTON_TEXT)
                            }
                        }
                    }
                    composable(SCREEN_2_ROUTE) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Button(onClick = { navController.popBackStack() }) {
                                Text(text = MOVE_BACK_BUTTON_TEXT)
                            }
                        }
                    }
                }
            }
        }
    }

    // 키보드는 최초 실행 시에만 보여야 함
    @Test
    fun showKeyboardOnceTest() =
        showKeyboardOnceTest(
            move = { moveButton.performClick() },
            moveBack = { moveBackButton.performClick() }
        )

    @Test
    fun inputChangeTest() =
        inputChangeTest(
            textField = rule.emailTextField,
            rule = rule,
            input = { emailInput },
            restoreTester = restoreTester
        )

    @Test
    fun textFieldVisibleTest() =
        textFieldVisibleTest(
            rule = rule,
            input = { passwordInput },
            restoreTester = restoreTester
        )

    @Test
    fun cancelIconTest() =
        cancelIconTest(
            cancelIconTestField = rule.emailTextField,
            noCancelIconTestField = rule.passwordTextField,
            rule = rule,
            cancelIconTextFieldInput = { emailInput },
            restorationTester = restoreTester
        )

    @Test
    fun errorTest() = errorTest(
        rule = rule,
        errorTextField = rule.emailTextField,
        setError = { state.error = error_test },
        restoreTester = restoreTester
    )

    @Test
    fun cursorTest() {
        rule.visibleIcon.performClick()
        cursorTest(
            restoreTester = restoreTester,
            textField1 = rule.emailTextField,
            textField2 = rule.passwordTextField
        )
        cursorTest(
            restoreTester = restoreTester,
            textField1 = rule.passwordTextField,
            textField2 = rule.emailTextField
        )
    }

    @Test
    fun imeTest() = imeTest(rule.emailTextField, doneTextField = rule.passwordTextField)

    private fun emailTextField(): @Composable () -> Unit = {
        state = rememberMaxWidthTextFieldState(
            label = email,
            showKeyboard = true,
            imeActionRoute = ImeActionRoute.NEXT
        )

        MaxWidthTextFieldWithError(
            state = state,
            onValueChange = state::onValueChange,
            onFocusChanged = state::onFocusChanged,
            onInputChange = { emailInput = it }
        )
    }

    private fun passwordTextField(): @Composable () -> Unit = {
        passwordState = rememberMaxWidthTextFieldState(
            label = password,
            initialVisibility = false,
            keyboardRoute = KeyboardRoute.PASSWORD,
            imeActionRoute = ImeActionRoute.DONE,
            cancelIconEnabled = false
        )

        MaxWidthTextFieldWithError(
            state = passwordState,
            onValueChange = passwordState::onValueChange,
            onFocusChanged = passwordState::onFocusChanged,
            onInputChange = { passwordInput = it },
            trailingIcon = {
                VisibleIcon(
                    isVisible = { passwordState.isVisible },
                    onClick = passwordState::visibilityToggle
                )
            }
        )
    }
}