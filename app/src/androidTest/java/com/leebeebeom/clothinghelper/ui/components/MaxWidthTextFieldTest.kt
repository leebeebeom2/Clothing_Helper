package com.leebeebeom.clothinghelper.ui.components

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

class MaxWidthTextFieldTest {
    private lateinit var emailInput: String
    private lateinit var passwordInput: String
    private lateinit var emailState: MutableMaxWidthTextFieldState
    private lateinit var passwordState: MutableMaxWidthTextFieldState
    private val moveButton get() = customTestRule.getNodeWithText(MOVE_BUTTON_TEXT)
    private val moveBackButton get() = customTestRule.getNodeWithText(MOVE_BACK_BUTTON_TEXT)

    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)

    @Before
    fun init() {
        emailInput = ""
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
                            emailTextField()()
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

    @Test
    fun showKeyboardTest() = showKeyboardTest(textField = customTestRule.emailTextField)

    // 키보드는 최초 실행 시에만 보여야 함
    @Test
    fun showKeyboardOnceTest() = showKeyboardOnceTest(
        focusNode = { customTestRule.emailTextField },
        rule = customTestRule,
        firstScreenTag = FIRST_SCREEN_TAG,
        secondScreenTag = SECOND_SCREEN_TAG,
        move = { moveButton.click() },
        moveBack = { moveBackButton.click() },
    )

    @Test
    fun inputChangeTest() {
        inputChangeTest(
            rule = customTestRule,
            textField = { customTestRule.emailTextField },
            input = { emailInput }
        )
        invisibleTest(customTestRule) {
            inputChangeTest(
                rule = customTestRule,
                textField = { customTestRule.passwordTextField },
                input = { passwordInput },
                invisible = it
            )
        }
    }

    @Test
    fun textFieldVisibleTest() = textFieldVisibleTest(rule = customTestRule,
        textField = { customTestRule.passwordTextField })

    @Test
    fun cancelIconTest() = cancelIconTest(rule = customTestRule,
        cancelIconTextField = { customTestRule.emailTextField },
        noCancelIconTextField = { customTestRule.passwordTextField })

    @Test
    fun errorTest() {
        errorTest(
            rule = customTestRule,
            errorTextField = { customTestRule.emailTextField },
            errorText = rule.activity.getString(error_test),
            setError = { emailState.error = error_test },
            blockBlank = true
        )
        invisibleTest(customTestRule) {
            errorTest(
                rule = customTestRule,
                errorTextField = { customTestRule.passwordTextField },
                errorText = rule.activity.getString(error_test),
                setError = { passwordState.error = error_test },
                blockBlank = true,
                invisible = it
            )
        }
    }

    @Test
    fun cursorTest() {
        cursorTest(customTestRule,
            textField1 = { customTestRule.emailTextField },
            textField2 = { customTestRule.passwordTextField })

        invisibleTest(customTestRule) {
            cursorTest(
                customTestRule,
                textField1 = { customTestRule.passwordTextField },
                textField2 = { customTestRule.emailTextField },
                invisible = it
            )
        }

    }

    @Test
    fun imeTest() = imeTest({ customTestRule.emailTextField },
        doneTextField = { customTestRule.passwordTextField })

    @Test
    fun blockBlankTest() {
        blockBlankTest(rule = customTestRule, textField = { customTestRule.emailTextField })
        invisibleTest(customTestRule) {
            blockBlankTest(
                rule = customTestRule,
                textField = { customTestRule.passwordTextField },
                invisible = it)
        }
    }


    private fun emailTextField(): @Composable () -> Unit = {
        emailState = rememberMaxWidthTextFieldState(
            label = email,
            showKeyboard = true,
            imeActionRoute = ImeActionRoute.NEXT,
            blockBlank = true
        )

        MaxWidthTextFieldWithError(state = emailState,
            onValueChange = emailState::onValueChange,
            onFocusChanged = emailState::onFocusChanged,
            onInputChange = {
                if (emailInput != it) {
                    emailInput = it
                    emailState.error = null
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

fun showKeyboardTest(textField: CustomSemanticsNodeInteraction) {
    textField.focused()
    isKeyboardShown()
}

fun showKeyboardOnceTest(
    rule: CustomTestRule,
    focusNode: () -> CustomSemanticsNodeInteraction,
    firstScreenTag: String,
    secondScreenTag: String,
    move: () -> Unit,
    moveBack: () -> Unit
) {
    focusNode().focused()
    isKeyboardShown()

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
    fun inputText() =
        if (invisible) textField().invisibleInput(TEST_INPUT)
        else textField().input(TEST_INPUT)

    fun checkTextFieldInput(text: String) {
        if (invisible) {
            rule.getInvisibleTextNode(text).exist()
            rule.visibleIcon.click()
        }
        rule.getNodeWithText(text).exist()
        if (invisible) rule.invisibleIcon.click()
    }

    fun checkInputSame(compareText: String) = assert(input() == compareText)
    fun checkInitState() {
        checkInputSame(compareText = "")
        rule.testInputNode().notExist()
        checkInputSame(compareText = "")
        rule.cursorInputNode().notExist()
    }

    checkInitState()

    inputText()

    checkInputSame(compareText = TEST_INPUT)

    checkTextFieldInput(TEST_INPUT)

    checkInputSame(compareText = TEST_INPUT)

    textField().click()
    textField().inputWithNoRestore(index = 1, text = CURSOR_INPUT_TEXT)

    checkInputSame(compareText = CURSOR_INPUT)

    checkTextFieldInput(CURSOR_INPUT)

    checkInputSame(compareText = CURSOR_INPUT)

    if (invisible)
        textField().textClear(beforeText = rule.getInvisibleText(TEST_INPUT))
    else textField().textClear(beforeText = TEST_INPUT)

    checkInitState()
}

fun textFieldVisibleTest(
    rule: CustomTestRule, textField: () -> CustomSemanticsNodeInteraction
) {
    fun visibleIconExist() {
        rule.visibleIcon.exist()
        rule.invisibleIcon.notExist()
    }

    fun invisibleIconExist() {
        rule.visibleIcon.notExist()
        rule.invisibleIcon.exist()
    }

    fun invisibleTextNode() = rule.getInvisibleTextNode(TEST_INPUT)

    visibleIconExist()
    rule.testInputNode().notExist()

    textField().invisibleInput(TEST_INPUT)
    rule.testInputNode().notExist()

    rule.visibleIcon.click()
    invisibleIconExist()
    invisibleTextNode().notExist()
    rule.testInputNode().exist()

    rule.invisibleIcon.click()
    visibleIconExist()
    invisibleTextNode().exist()
    rule.testInputNode().notExist()
}

fun cancelIconTest(
    rule: CustomTestRule,
    cancelIconTextField: () -> CustomSemanticsNodeInteraction,
    noCancelIconTextField: () -> CustomSemanticsNodeInteraction
) {
    fun cancelIconNotExist() = rule.restore {
        rule.cancelIcon.notExist(false)
        noCancelIconTextField().click() // loose focus
        rule.cancelIcon.notExist()
        cancelIconTextField().click() // get focus
        rule.cancelIcon.notExist(false)
    }

    fun cancelIconExist() = rule.restore {
        cancelIconTextField().click()
        rule.cancelIcon.exist(false)
        noCancelIconTextField().click()
        rule.cancelIcon.notExist(false)
        cancelIconTextField().click()
        rule.cancelIcon.exist(false)
    }

    cancelIconNotExist() // 초기 화면

    cancelIconTextField().input(TEST_INPUT) // 인풋 입력

    cancelIconTextField().click() // get focus(포커스 없을 시 캔슬 아이콘 사라짐)
    rule.cancelIcon.exist(false) // 캔슬 아이콘 생성
    rule.restore() // 액티비티 재구성
    rule.cancelIcon.notExist(false) // 캔슬 아이콘 사라짐(loose focus)
    cancelIconExist() // 캔슬 아이콘 유지 확인

    rule.cancelIcon.click() // 캔슬 아이콘 기능 확인

    rule.restore {
        rule.testInputNode().notExist(false) // 인풋 사라짐 확인
        cancelIconTextField().click() // get focus
        rule.cancelIcon.notExist(false) // 캔슬 아이콘 사라짐 확인
        noCancelIconTextField().click() // loose focus
        rule.cancelIcon.notExist(false) // 캔슬 아이콘 사라짐 확인
        cancelIconTextField().click() // get focus
        rule.cancelIcon.notExist(false) // 캔슬 아이콘 사라짐 확인
    }

    cancelIconTextField().input(TEST_INPUT)
    cancelIconExist()

    cancelIconTextField().textClear(TEST_INPUT)
    cancelIconNotExist() // 캔슬 아이콘 사라짐 확인
}

fun errorTest(
    rule: CustomTestRule,
    errorTextField: () -> CustomSemanticsNodeInteraction,
    errorText: String,
    setError: () -> Unit,
    blockBlank: Boolean = false,
    invisible: Boolean = false
) {
    fun errorTextNode() = rule.getNodeWithText(errorText)

    fun errorNotExist() = errorTextNode().notExist()

    fun errorExist() = errorTextNode().exist()

    errorNotExist()

    setError()

    errorExist()

    if (blockBlank) {
        errorTextField().inputWithNoRestore(0, " ")
        errorExist()
    }

    if (invisible) errorTextField().invisibleInput(TEST_INPUT)
    else errorTextField().input(TEST_INPUT)

    errorNotExist()
    errorTextField().textClear(if (invisible) rule.getInvisibleText(TEST_INPUT) else TEST_INPUT)
}

fun cursorTest(
    rule: CustomTestRule,
    textField1: () -> CustomSemanticsNodeInteraction,
    textField2: () -> CustomSemanticsNodeInteraction,
    invisible: Boolean = false,
) {
    fun changeFocus() {
        textField2().click()
        textField1().notFocused()
        textField2().focused()

        textField1().click()
        textField1().focused()
        textField2().notFocused()
    }

    fun existTextCheck(text: String) {
        if (invisible) rule.getInvisibleTextNode(text).exist()
        else rule.getNodeWithText(text).exist()
    }

    if (invisible) textField1().invisibleInput(TEST_INPUT)
    else textField1().input(TEST_INPUT)

    changeFocus()
    textField1().inputWithNoRestore(TEST_INPUT.length, CURSOR_INPUT_TEXT)

    existTextCheck(TEST_INPUT + CURSOR_INPUT_TEXT)

    repeat(2) { changeFocus() }

    textField1().inputWithNoRestore((TEST_INPUT + CURSOR_INPUT_TEXT).length, CURSOR_INPUT_TEXT)

    existTextCheck(TEST_INPUT + CURSOR_INPUT_TEXT + CURSOR_INPUT_TEXT)

    textField1().textClear()
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
}

fun blockBlankTest(
    rule: CustomTestRule,
    textField: () -> CustomSemanticsNodeInteraction,
    invisible: Boolean = false
) {
    fun notExistText(text: String) =
        if (invisible) rule.getInvisibleTextNode(text).notExist()
        else rule.getNodeWithText(text).notExist()

    if (invisible) textField().invisibleInput(TEST_INPUT)
    else textField().input(TEST_INPUT)

    textField().inputWithNoRestore(0, " ")

    notExistText(" $TEST_INPUT")

    textField().inputWithNoRestore(6, " ")

    notExistText("$TEST_INPUT ")

    textField().textClear(if (invisible) rule.getInvisibleText(TEST_INPUT) else TEST_INPUT)
}

fun invisibleTest(rule: CustomTestRule, test: (Boolean) -> Unit) {
    test(true)
    rule.visibleIcon.click()
    test(false)
}

private fun CustomTestRule.testInputNode() = getNodeWithText(TEST_INPUT)
private fun CustomTestRule.cursorInputNode() = getNodeWithText(CURSOR_INPUT)