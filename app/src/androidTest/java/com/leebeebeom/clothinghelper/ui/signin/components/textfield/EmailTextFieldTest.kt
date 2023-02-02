package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextRange
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.CANCEL_ICON_TAG
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute
import com.leebeebeom.clothinghelper.ui.components.MutableMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EmailTextFieldTest {
    @get:Rule
    val rule = createComposeRule()
    private lateinit var state: MutableMaxWidthTextFieldState
    private var input = ""
    private val restoreTester = StateRestorationTester(rule)
    private val testInput = "테스트"

    @Before
    fun init() {
        input = ""

        restoreTester.setContent {
            ClothingHelperTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    state = rememberEmailTextFieldState(imeActionRoute = ImeActionRoute.DONE)

                    EmailTextField(
                        state = state,
                        error = { state.error },
                        onInputChange = { input = it }
                    )
                }
            }
        }
    }

    @Test
    fun inputChangeTest() {
        textField.performTextInput(testInput)

        assertInput(testInput)
        restoreTester.emulateSavedInstanceStateRestore()
        assertInput(testInput)

        rule.waitForIdle()
        textField.performClick() // 포커스 이동
        // 커서 변경
        state.onValueChange(newTextField = state.textFieldValue.copy(selection = TextRange(1)))

        textField.performTextInput("스")

        assertInput("테스스트")
        restoreTester.emulateSavedInstanceStateRestore()
        assertInput("테스스트")
    }

    @Test
    fun cancelIconTest() {
        cancelIcon.assertDoesNotExist()
        textField.performTextInput(testInput)
        rule.waitForIdle()
        cancelIcon.assertExists()

        textField.performImeAction()
        cancelIcon.assertDoesNotExist()

        assertInput(testInput)
        restoreTester.emulateSavedInstanceStateRestore()
        assertInput(testInput)

        cancelIcon.assertDoesNotExist()
        textField.performClick()
        cancelIcon.assertExists()
        cancelIcon.performClick()

        assertInput("")
        cancelIcon.assertDoesNotExist()
        restoreTester.emulateSavedInstanceStateRestore()
        assertInput("")
        cancelIcon.assertDoesNotExist()
    }

    @Test
    fun errorTest() {
        errorText.assertDoesNotExist()
        state.error = R.string.error_test

        errorText.assertExists()
        restoreTester.emulateSavedInstanceStateRestore()
        errorText.assertExists()

        textField.performTextInput(" ") // block blank
        errorText.assertExists()
        restoreTester.emulateSavedInstanceStateRestore()
        textField.performTextInput(" ") // block blank
        errorText.assertExists()

        textField.performTextInput("0")

        errorText.assertDoesNotExist()
        restoreTester.emulateSavedInstanceStateRestore()
        errorText.assertDoesNotExist()
    }

    @Test
    fun blockBlankTest() {
        textField.performTextInput(testInput)

        assertBlockBlank()
        restoreTester.emulateSavedInstanceStateRestore()
        assertBlockBlank()
    }

    private fun assertBlockBlank() {
        textField.performTextInput(" ")
        rule.onNodeWithText("$testInput ").assertDoesNotExist()
        rule.onNodeWithText(testInput).assertExists()
    }

    private fun assertInput(text: String) {
        rule.onNodeWithText(text).assertExists()
        assert(input == text)
    }

    private val textField get() = rule.onNodeWithText("이메일")
    private val cancelIcon get() = rule.onNodeWithContentDescription(CANCEL_ICON_TAG)
    private val errorText get() = rule.onNodeWithText("에러")
}