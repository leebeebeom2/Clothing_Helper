package com.leebeebeom.clothinghelper.ui.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.isKeyboardShowing
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.component.MaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.component.MaxWidthTextFieldWithError
import com.leebeebeom.clothinghelper.ui.component.rememberMaxWidthTestFieldState
import com.leebeebeom.clothinghelper.ui.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.waitStringResExist
import com.leebeebeom.clothinghelper.ui.waitTextExist
import org.junit.Before
import org.junit.Rule
import org.junit.Test

const val TestText = "테스트 텍스트"
const val TestText2 = "테스트 텍스트2"

class MaxWidthTextFieldWithErrorTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private var textField1input = ""
    private lateinit var textField1state: MaxWidthTextFieldState
    private var textField2input = ""
    private lateinit var textField2state: MaxWidthTextFieldState
    private val testTextField1 by lazy { rule.onNodeWithStringRes(R.string.test_text_field) }
    private val testTextField2 by lazy { rule.onNodeWithStringRes(R.string.test_text_field2) }
    private var error: Int? by mutableStateOf(null)
    private var error2: Int? by mutableStateOf(null)
    private val screen1Route = "screen1"
    private val screen2Route = "screen2"

    @Before
    fun init() {
        restorationTester.setContent {
            ClothingHelperTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = screen1Route,
                ) {
                    composable(route = screen1Route) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            textField1state = rememberMaxWidthTestFieldState(
                                initialText = textField1input,
                                blockBlank = true
                            )

                            MaxWidthTextFieldWithError(
                                textFieldValue = { textField1state.textFieldValue },
                                onValueChange = textField1state::onValueChange,
                                onFocusChanged = textField1state::onFocusChanged,
                                error = { error },
                                label = R.string.test_text_field,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                                ),
                                onInputChange = { textField1input = it },
                                showKeyboard = true,
                                fixedError = false
                            )

                            textField2state =
                                rememberMaxWidthTestFieldState(initialText = textField2input)
                            MaxWidthTextFieldWithError(
                                textFieldValue = { textField2state.textFieldValue },
                                onValueChange = textField2state::onValueChange,
                                onFocusChanged = textField2state::onFocusChanged,
                                error = { error2 },
                                label = R.string.test_text_field2,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                                ),
                                onInputChange = { textField2input = it },
                                showKeyboard = false,
                                fixedError = false
                            )

                            Button(onClick = { navController.navigate(screen2Route) }) {
                                Text(text = "이동")
                            }
                        }
                    }
                    composable(route = screen2Route) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Button(onClick = navController::popBackStack) {
                                Text(text = "뒤로 이동")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun onFocusChangeTest() {
        // 포커스 잡히면 텍스트 맨 뒤로
        testTextField1.performTextInput(TestText)
        testTextField2.performTextInput(TestText2)

        repeat(2) {
            testTextField1.performClick()
            assert(textField1state.textFieldValue.selection == TextRange(textField1state.textFieldValue.text.length))
            testTextField2.performClick()
            assert(textField2state.textFieldValue.selection == TextRange(textField2state.textFieldValue.text.length))
        }
    }

    @Test
    fun getFocusAndImeTest() {
        testTextField1.assertIsFocused()
        assert(isKeyboardShowing())
        testTextField1.performImeAction()
        testTextField2.assertIsFocused()
        testTextField2.performImeAction()
        testTextField2.assertIsNotFocused()
        assert(!isKeyboardShowing())
    }

    @Test
    fun errorTest() {
        error = R.string.error_invalid_email
        error2 = R.string.error_wrong_password

        rule.onNodeWithStringRes(R.string.error_invalid_email).assertExists()
        rule.onNodeWithStringRes(R.string.error_wrong_password).assertExists()

        error = null
        error2 = null

        rule.onNodeWithStringRes(R.string.error_invalid_email).assertDoesNotExist()
        rule.onNodeWithStringRes(R.string.error_wrong_password).assertDoesNotExist()
    }

    @Test
    fun showKeyboardTest() {
        testTextField1.assertIsFocused()
        assert(isKeyboardShowing())
        device.pressBack()
        rule.onNodeWithText("이동").performClick()
        rule.waitTextExist("뒤로 이동")
        rule.onNodeWithText("뒤로 이동").performClick()
        rule.waitStringResExist(R.string.test_text_field)
        testTextField1.assertIsNotFocused()
        assert(!isKeyboardShowing())
    }

    @Test
    fun inputChangeTest() {
        testTextField1.performTextInput(TestText)
        assert(textField1input == TestText)
        testTextField2.performTextInput(TestText2)
        assert(textField2input == TestText2)
    }

    @Test
    fun blockBlankTest() {
        testTextField1.performTextInput(TestText)
        repeat(3) { testTextField1.performTextInput(" ") }
        testTextField1.assert(hasText(TestText))
        assert(textField1input == TestText)
    }
}