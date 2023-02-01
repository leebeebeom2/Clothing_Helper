package com.leebeebeom.clothinghelper.ui.components

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.VisibleIcon
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MaxWidthTextFieldTest {
    private val moveText = "이동"
    private val moveBackText = "뒤로 이동"
    private val testInput = "테스트 인풋"
    private var input = ""
    private val screen1Route = "screen1"
    private val screen2Route = "screen2"
    private lateinit var state: MutableMaxWidthTextFieldState
    private lateinit var passwordState: MutableMaxWidthTextFieldState

    @get:Rule
    val rule = createComposeRule()
    lateinit var recreateTester: StateRestorationTester

    @Before
    fun init() {
        input = ""

        recreateTester = StateRestorationTester(rule)

        recreateTester.setContent {
            ClothingHelperTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = screen1Route) {
                    composable(screen1Route) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            emailTextField()()
                            passwordTextField()()

                            Button(onClick = { navController.navigate(screen2Route) }) {
                                Text(text = moveText)
                            }
                        }
                    }
                    composable(screen2Route) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Button(onClick = { navController.popBackStack() }) {
                                Text(text = moveBackText)
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun showKeyboardTest() {
        // 키보드는 최초 실행 시에만 보여야 함
        assert(isKeyboardShown())
        moveButton.performClick()
        moveBackButton.performClick()
        assert(!isKeyboardShown())
    }

    @Test
    fun textChangeTest() {
        textField.performTextInput(testInput)

        assertInputChange()
        recreateTester.emulateSavedInstanceStateRestore()
        assertInputChange()

        textField.performClick() // 포커스
        // 커서 위치 변경
        state.onValueChange(newTextField = state.textFieldValue.copy(selection = TextRange(1)))
        textField.performTextInput("스")

        assertCursorPosition()
        recreateTester.emulateSavedInstanceStateRestore()
        assertCursorPosition()

        textField.performTextClearance()

        assertTextClear()
        recreateTester.emulateSavedInstanceStateRestore()
        assertTextClear()
    }

    private fun assertTextClear() {
        rule.onNodeWithText(testInput).assertDoesNotExist()
        assert(input.isBlank())
    }

    private fun assertCursorPosition() {
        rule.onNodeWithText("테스스트 인풋").assertExists()
        assert(input == "테스스트 인풋")
    }

    private fun assertInputChange() {
        rule.onNodeWithText(testInput).assertExists()
        assert(input == testInput)
    }

    @Test
    fun passwordTextFieldTest() {
        passwordTextField.performTextInput(testInput)

        assertInVisible()
        recreateTester.emulateSavedInstanceStateRestore()
        assertInVisible()

        visibleIcon.performClick()


        assertVisible()
        recreateTester.emulateSavedInstanceStateRestore()
        assertVisible()

        invisibleIcon.performClick()

        assertInVisible()
        recreateTester.emulateSavedInstanceStateRestore()
        assertInVisible()
    }

    private fun assertVisible() {
        visibleIcon.assertDoesNotExist()
        invisibleIcon.assertExists()
        rule.onNodeWithText(testInput).assertExists()
    }

    private fun assertInVisible() {
        visibleIcon.assertExists()
        invisibleIcon.assertDoesNotExist()
        rule.onNodeWithText(testInput).assertDoesNotExist()
    }

    @Test
    fun cancelIconTest() {
        textField.performTextInput(testInput)
        cancelIcon.assertExists()

        passwordTextField.performClick()
        cancelIcon.assertDoesNotExist()

        textField.performClick()
        cancelIcon.assertExists()
        cancelIcon.performClick()

        rule.onNodeWithText(testInput).assertDoesNotExist()
    }

    @Test
    fun errorTest() {
        state.error = R.string.error_test
        rule.waitForIdle()

        errorText.assertExists()
        recreateTester.emulateSavedInstanceStateRestore()
        errorText.assertExists()

        textField.performTextInput(testInput)

        errorText.assertDoesNotExist()
        recreateTester.emulateSavedInstanceStateRestore()
        errorText.assertDoesNotExist()
    }

    private val moveButton get() = rule.onNodeWithText(moveText)
    private val moveBackButton get() = rule.onNodeWithText(moveBackText)
    private val textField get() = rule.onNodeWithText("이메일")
    private val passwordTextField get() = rule.onNodeWithText("비밀번호")
    private val visibleIcon get() = rule.onNodeWithTag(visibleIconTag)
    private val invisibleIcon get() = rule.onNodeWithTag(invisibleIconTag)
    private val cancelIcon get() = rule.onNodeWithContentDescription(CANCEL_ICON)
    private val errorText get() = rule.onNodeWithText("에러")

    /**
     * showKeyboard
     */
    private fun emailTextField(): @Composable () -> Unit = {
        state = rememberMaxWidthTextFieldState(
            label = R.string.email,
            showKeyboard = true,
            imeActionRoute = ImeActionRoute.NEXT
        )

        MaxWidthTextFieldWithError(
            state = state,
            onValueChange = state::onValueChange,
            onFocusChanged = state::onFocusChanged,
            onInputChange = { input = it }
        )
    }

    private val visibleIconTag = "visible icon"
    private val invisibleIconTag = "invisible icon"

    private fun passwordTextField(): @Composable () -> Unit = {
        passwordState = rememberMaxWidthTextFieldState(
            label = R.string.password,
            initialVisibility = false,
            keyboardRoute = KeyboardRoute.PASSWORD,
            imeActionRoute = ImeActionRoute.DONE,
            cancelIconEnabled = false
        )

        MaxWidthTextFieldWithError(
            state = passwordState,
            onValueChange = passwordState::onValueChange,
            onFocusChanged = passwordState::onFocusChanged,
            onInputChange = {},
            trailingIcon = {
                VisibleIcon(
                    modifier = Modifier.testTag(
                        if (passwordState.isVisible) invisibleIconTag
                        else visibleIconTag
                    ),
                    isVisible = { passwordState.isVisible },
                    onClick = passwordState::visibilityToggle
                )
            }
        )
    }
}

fun isKeyboardShown(): Boolean {
    val inputMethodManager =
        InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    KeyboardType
    return inputMethodManager.isAcceptingText
}