package com.leebeebeom.clothinghelper.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.CustomTestRule.CustomSemanticsNodeInteraction
import com.leebeebeom.clothinghelper.R.string.*
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.VisibleIcon
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val MOVE_BUTTON_TEXT = "이동"
private const val MOVE_BACK_BUTTON_TEXT = "뒤로 이동"
private const val FIRST_SCREEN_ROUTE = "screen1"
private const val SECOND_SCREEN_ROUTE = "screen2"
private const val FIRST_SCREEN_TAG = "screen1"
private const val SECOND_SCREEN_TAG = "screen2"
private const val TEST_INPUT = "테스트 인풋"
private const val CURSOR_INPUT_TEXT = "스"
private const val CURSOR_INPUT = "테스스트 인풋"

class MaxWidthTextFieldTest {
    private lateinit var input: String
    private lateinit var passwordInput: String
    private lateinit var state: MutableMaxWidthTextFieldState
    private lateinit var passwordState: MutableMaxWidthTextFieldState
    private val moveButton get() = customTestRule.getNodeWithText(MOVE_BUTTON_TEXT)
    private val moveBackButton get() = customTestRule.getNodeWithText(MOVE_BACK_BUTTON_TEXT)

    private var showKeyboard = true
    private var cancelIconEnable = true

    private val textField get() = customTestRule.getNodeWithStringRes(text_field)
    private val passwordTextField get() = passwordTextField(customTestRule)

    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)

    @Before
    fun init() {
        input = ""
        passwordInput = ""

        customTestRule.setContent {
            ClothingHelperTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = FIRST_SCREEN_ROUTE) {
                    composable(FIRST_SCREEN_ROUTE) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag(FIRST_SCREEN_TAG),
                            verticalArrangement = Arrangement.Center
                        ) {
                            textField()()
                            passwordTextField()()

                            Button(onClick = { navController.navigate(SECOND_SCREEN_ROUTE) }) {
                                Text(text = MOVE_BUTTON_TEXT)
                            }
                        }
                    }
                    composable(SECOND_SCREEN_ROUTE) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag(SECOND_SCREEN_TAG)
                        ) {
                            Button(onClick = { navController.popBackStack() }) {
                                Text(text = MOVE_BACK_BUTTON_TEXT)
                            }
                        }
                    }
                }
            }
        }
    }

    @Test // 쇼키보드, 노쇼키보드 둘 다 테스트
    fun showKeyboardTest() = showKeyboardTest(textField = textField, showKeyboard = showKeyboard)

    // 키보드는 최초 실행 시에만 보여야 함
    @Test
    fun showKeyboardOnceTest() = showKeyboardOnceTest(
        focusNode = { textField },
        rule = customTestRule,
        firstScreenTag = FIRST_SCREEN_TAG,
        secondScreenTag = SECOND_SCREEN_TAG,
        move = { moveButton.click() },
        moveBack = { moveBackButton.click() },
    )

    @Test
    fun inputChangeTest() =
        inputChangeTest(rule = customTestRule, textField = { textField }, input = { input })

    @Test
    fun textFieldVisibleTest() = textFieldVisibleTest(rule = customTestRule,
        textField = { passwordTextField },
        input = { passwordInput })

    @Test
    fun cancelIconTest() = // enable unable 둘 다 테스트
        cancelIconTest(
            rule = customTestRule,
            cancelIconTextField = { textField },
            looseFocus = { passwordTextField.click() },
            enable = cancelIconEnable
        )

    @Test
    fun errorTest() = errorTest(
        rule = customTestRule,
        errorTextField = { textField },
        errorTextRes = error_test,
        setError = { state.error = error_test },
        blockBlank = true
    )

    @Test
    fun cursorTest() =
        cursorTest(customTestRule,
            textField = { textField },
            looseFocus = { passwordTextField.click() })

    @Test
    fun imeTest() = imeTest({ textField }, doneTextField = { passwordTextField })

    @Test
    fun blockBlankTest() = blockBlankTest(rule = customTestRule, textField = { textField })

    private fun textField(): @Composable () -> Unit = {
        state = rememberMaxWidthTextFieldState(
            label = text_field,
            showKeyboard = showKeyboard,
            imeActionRoute = ImeActionRoute.NEXT,
            blockBlank = true,
            cancelIconEnabled = cancelIconEnable
        )

        MaxWidthTextFieldWithError(state = state,
            onValueChange = state::onValueChange,
            onFocusChanged = state::onFocusChanged,
            onInputChange = {
                if (input != it) {
                    input = it
                    state.error = null
                }
            })
    }

    private fun passwordTextField(): @Composable () -> Unit = {
        passwordState = rememberMaxWidthTextFieldState(
            label = password,
            initialVisibility = false,
            keyboardRoute = KeyboardRoute.PASSWORD,
            imeActionRoute = ImeActionRoute.DONE,
            cancelIconEnabled = false,
            blockBlank = true
        )

        MaxWidthTextFieldWithError(state = passwordState,
            onValueChange = passwordState::onValueChange,
            onFocusChanged = passwordState::onFocusChanged,
            onInputChange = {
                if (passwordInput != it) {
                    passwordInput = it
                    passwordState.error = null
                }
            },
            trailingIcon = {
                VisibleIcon(
                    isVisible = { passwordState.isVisible },
                    onClick = passwordState::visibilityToggle
                )
            })
    }
}

fun showKeyboardTest(textField: CustomSemanticsNodeInteraction, showKeyboard: Boolean = true) =
    if (showKeyboard) {
        textField.focused()
        isKeyboardShown()
    } else {
        textField.notFocused()
        isKeyboardNotShown()
    }

fun showKeyboardOnceTest(
    rule: CustomTestRule,
    focusNode: () -> CustomSemanticsNodeInteraction,
    firstScreenTag: String,
    secondScreenTag: String,
    move: () -> Unit,
    moveBack: () -> Unit
) {
    showKeyboardTest(textField = focusNode())

    move()
    rule.getNodeWithTag(secondScreenTag).exist()
    moveBack()
    rule.getNodeWithTag(firstScreenTag).exist()

    isKeyboardNotShown()
}

fun inputChangeTest(
    rule: CustomTestRule,
    textField: () -> CustomSemanticsNodeInteraction,
    input: () -> String,
    invisible: Boolean = false
) {
    fun input(text: String, beforeText: String = "") =
        textField().input(text = text, beforeText = beforeText, invisible = invisible)

    fun exist(text: String) = rule.getNodeWithText(text = text, invisible = invisible).exist()

    fun checkInputSame(compareText: String) = assert(input() == compareText)

    fun checkInitState() {
        checkInputSame(compareText = "")
        rule.getNodeWithText(TEST_INPUT, invisible = invisible).notExist()
        checkInputSame(compareText = "")
        rule.getNodeWithText(CURSOR_INPUT, invisible = invisible).notExist()
    }

    checkInitState()

    input(text = TEST_INPUT)

    checkInputSame(compareText = TEST_INPUT)

    exist(text = TEST_INPUT)

    checkInputSame(compareText = TEST_INPUT)

    input(text = CURSOR_INPUT_TEXT, beforeText = TEST_INPUT)

    checkInputSame(compareText = TEST_INPUT + CURSOR_INPUT_TEXT)

    exist(text = TEST_INPUT + CURSOR_INPUT_TEXT)

    checkInputSame(compareText = TEST_INPUT + CURSOR_INPUT_TEXT)

    textField().click()
    textField().inputWithNoRestore(index = 1, text = CURSOR_INPUT_TEXT)

    checkInputSame(compareText = CURSOR_INPUT + CURSOR_INPUT_TEXT)

    exist(text = CURSOR_INPUT + CURSOR_INPUT_TEXT)

    checkInputSame(compareText = CURSOR_INPUT + CURSOR_INPUT_TEXT)

    textField().textClear(beforeText = CURSOR_INPUT + CURSOR_INPUT_TEXT, invisible = invisible)

    checkInitState()
}

fun textFieldVisibleTest(
    rule: CustomTestRule,
    textField: () -> CustomSemanticsNodeInteraction,
    input: () -> String,
    visibleIcon: () -> CustomSemanticsNodeInteraction = { visibleIcon(rule) },
    invisibleIcon: () -> CustomSemanticsNodeInteraction = { invisibleIcon(rule) }
) {
    fun inputCheck(text: String) = assert(input() == text)

    fun visibleIconExist() {
        visibleIcon().exist()
        invisibleIcon().notExist()
    }

    fun invisibleIconExist() {
        visibleIcon().notExist()
        invisibleIcon().exist()
    }

    fun visibleTextNode(text: String, invisible: Boolean = false) =
        rule.getNodeWithText(text = text, invisible = invisible)

    fun invisibleTextNode(text: String) = visibleTextNode(text = text, invisible = true)

    fun visible(text: String) {
        visibleIcon().click()
        invisibleIconExist()
        invisibleTextNode(text = text).notExist()
        visibleTextNode(text = text).exist()
    }

    fun invisible(text: String) {
        invisibleIcon().click()
        visibleIconExist()
        invisibleTextNode(text = text).exist()
        visibleTextNode(text = text).notExist()
    }

    fun invisibleInput(text: String, beforeText: String = "") {
        textField().input(text = text, beforeText = beforeText, invisible = true)
        inputCheck(beforeText + text)
        invisibleTextNode(text = beforeText + text).exist()
        visibleTextNode(text = beforeText + text).notExist()
    }

    fun visibleInput(text: String, beforeText: String = "") {
        textField().input(text = text, beforeText = beforeText, invisible = false)
        inputCheck(beforeText + text)
        invisibleTextNode(text = beforeText + text).notExist()
        visibleTextNode(text = beforeText + text).exist()
    }

    visibleIconExist()
    invisibleTextNode(TEST_INPUT).notExist()
    visibleTextNode(TEST_INPUT).notExist()

    invisibleInput(TEST_INPUT)

    visible(TEST_INPUT)
    invisible(TEST_INPUT)
    visible(TEST_INPUT)

    visibleInput(text = CURSOR_INPUT_TEXT, beforeText = TEST_INPUT)
    invisible(TEST_INPUT + CURSOR_INPUT_TEXT)
    visible(TEST_INPUT + CURSOR_INPUT_TEXT)
    invisible(TEST_INPUT + CURSOR_INPUT_TEXT)

    invisibleInput(text = CURSOR_INPUT_TEXT, beforeText = TEST_INPUT + CURSOR_INPUT_TEXT)
    visible(TEST_INPUT + CURSOR_INPUT_TEXT + CURSOR_INPUT_TEXT)
    invisible(TEST_INPUT + CURSOR_INPUT_TEXT + CURSOR_INPUT_TEXT)
    visible(TEST_INPUT + CURSOR_INPUT_TEXT + CURSOR_INPUT_TEXT)

    textField().textClear(beforeText = TEST_INPUT + CURSOR_INPUT_TEXT + CURSOR_INPUT_TEXT)
    inputCheck("")
    visibleInput(text = TEST_INPUT)
    invisible(text = TEST_INPUT)
    textField().textClear(beforeText = TEST_INPUT, invisible = false)
    inputCheck("")
}

fun cancelIconTest(
    rule: CustomTestRule,
    cancelIconTextField: () -> CustomSemanticsNodeInteraction,
    looseFocus: () -> Unit,
    enable: Boolean = true,
    invisible: Boolean = false
) {
    fun cancelIcon() = cancelIcon(rule)

    fun getFocus() = cancelIconTextField().click()

    cancelIcon().notExist() // init state
    rule.restore {
        getFocus()
        cancelIcon().notExist(false)
    }
    looseFocus()
    cancelIcon().notExist()

    cancelIconTextField().input( // input(create cancel icon)
        text = TEST_INPUT,
        restore = false,
        invisible = invisible
    )
    if (enable) {
        cancelIcon().exist(false)
        rule.restore()
        rule.restore {
            cancelIcon().notExist(false)
            getFocus()
            cancelIcon().exist(false)
            looseFocus()
            cancelIcon().notExist(false)
        }
    } else {
        cancelIcon().notExist(false)
        rule.restore()
        rule.restore {
            cancelIcon().notExist(false)
            getFocus()
            cancelIcon().notExist(false)
            looseFocus()
            cancelIcon().notExist(false)
        }
    }

    getFocus()
    if (enable) cancelIcon().click()
    else cancelIconTextField().textClear(beforeText = TEST_INPUT, invisible = invisible)

    rule.restore {
        rule.getNodeWithText(TEST_INPUT).notExist(false) // clear(remove cancel icon)
        cancelIcon().notExist(false)
        looseFocus()
        cancelIcon().notExist(false)
        getFocus()
        cancelIcon().notExist(false)
    }
}

fun errorTest(
    rule: CustomTestRule,
    errorTextField: () -> CustomSemanticsNodeInteraction,
    @StringRes errorTextRes: Int,
    setError: () -> Unit,
    beforeText: String = "",
    blockBlank: Boolean = false,
    invisible: Boolean = false,
    clearOtherTextField: () -> Unit = {}
) {
    val errorText = rule.getString(errorTextRes)

    fun errorTextNode() = rule.getNodeWithText(errorText)

    fun errorNotExist() = errorTextNode().notExist()

    fun errorExist() = rule.waitTextExist(errorText, 5000)

    fun textClear() = errorTextField().textClear(beforeText = TEST_INPUT, invisible = invisible)

    errorNotExist()

    setError()

    errorExist()

    if (blockBlank) {
        errorTextField().inputWithNoRestore(0, " ")
        errorExist()
    }

    errorTextField().input(text = TEST_INPUT, beforeText = beforeText, invisible = invisible)

    errorNotExist()

    textClear()
    clearOtherTextField()
}

fun cursorTest(
    rule: CustomTestRule,
    textField: () -> CustomSemanticsNodeInteraction,
    looseFocus: () -> Unit,
    invisible: Boolean = false,
) {
    fun changeFocus() {
        looseFocus()
        textField().notFocused()

        textField().click()
        textField().focused()
    }

    fun existTextCheck(text: String) =
        rule.getNodeWithText(text = text, invisible = invisible).exist()

    fun input(text: String, beforeText: String = "") =
        textField().input(text = text, beforeText = beforeText, invisible = invisible)

    input(text = TEST_INPUT)

    changeFocus()
    input(text = CURSOR_INPUT_TEXT, beforeText = TEST_INPUT)

    existTextCheck(TEST_INPUT + CURSOR_INPUT_TEXT)

    repeat(2) { changeFocus() }

    input(text = CURSOR_INPUT_TEXT, beforeText = TEST_INPUT + CURSOR_INPUT_TEXT)

    existTextCheck(TEST_INPUT + CURSOR_INPUT_TEXT + CURSOR_INPUT_TEXT)

    textField().textClear()
}

fun imeTest(
    vararg nextTextField: () -> CustomSemanticsNodeInteraction,
    doneTextField: () -> CustomSemanticsNodeInteraction
) {
    if (nextTextField.isEmpty()) throw java.lang.IllegalStateException("nextTextField is empty")

    nextTextField.first()().click() // get focus

    if (nextTextField.size == 1) {
        nextTextField.first()().focused()
        nextTextField.first()().imeAction()
    } else {
        for (index in 0..nextTextField.lastIndex) {
            nextTextField[index]().focused()
            nextTextField[index]().imeAction()
            if (index + 1 > nextTextField.lastIndex) break
            nextTextField[index + 1]().focused()
        }
    }

    doneTextField().focused()
    doneTextField().imeAction()
    assert(!keyboardCheck())
    doneTextField().notFocused()
}

fun blockBlankTest(
    rule: CustomTestRule,
    textField: () -> CustomSemanticsNodeInteraction,
    invisible: Boolean = false
) {
    fun notExistText(text: String) = rule.getNodeWithText(text, invisible).notExist()

    textField().input(text = TEST_INPUT, invisible = invisible)

    textField().inputWithNoRestore(0, " ")

    notExistText(" $TEST_INPUT")

    textField().inputWithNoRestore(6, " ")

    notExistText("$TEST_INPUT ")

    textField().textClear(beforeText = TEST_INPUT, invisible = invisible)
}

fun invisibleTest(rule: CustomTestRule, test: (Boolean) -> Unit) {
    test(true)
    visibleIcon(rule).click()
    test(false)
}