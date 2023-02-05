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
import androidx.compose.ui.test.performClick
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
    fun inputChangeTest() = inputChangeTest(rule = customTestRule,
        textField = { customTestRule.emailTextField },
        input = { emailInput })

    @Test
    fun textFieldVisibleTest() = textFieldVisibleTest(customTestRule)

    @Test
    fun cancelIconTest() = cancelIconTest(
        rule = customTestRule,
        cancelIconTextField = { customTestRule.emailTextField },
        noCancelIconTextField = { customTestRule.passwordTextField }
    )

    @Test
    fun errorTest() = errorTest(
        rule = customTestRule,
        errorTextField = { customTestRule.emailTextField },
        errorText = rule.activity.getString(error_test),
        setError = { emailState.error = error_test },
        blockBlank = true
    )

    @Test
    fun cursorTest() {
        rule.visibleIcon.performClick()
        cursorTest(
            customTestRule,
            textField1 = { customTestRule.emailTextField },
            textField2 = { customTestRule.passwordTextField }
        )
        customTestRule.emailTextField.textClear()
        cursorTest(
            customTestRule,
            textField1 = { customTestRule.passwordTextField },
            textField2 = { customTestRule.emailTextField }
        )
    }

    @Test
    fun imeTest() = imeTest({ customTestRule.emailTextField },
        doneTextField = { customTestRule.passwordTextField })

    @Test
    fun blockBlankTest() = blockBlankTest(
        rule = customTestRule,
        textField = { customTestRule.emailTextField }
    )

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
            }
        )
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
    rule: CustomTestRule, textField: () -> CustomSemanticsNodeInteraction, input: () -> String
) {
    fun inputSame(compareText: String) = assert(input() == compareText)
    fun initState() {
        inputSame(compareText = "")
        rule.testInputNode().notExist()
        inputSame(compareText = "")
        rule.cursorInputNode().notExist()
    }

    initState()

    textField().input(TEST_INPUT)

    inputSame(compareText = TEST_INPUT)
    rule.testInputNode().exist()
    inputSame(compareText = TEST_INPUT)

    textField().click()
    textField().inputWithNoRestore(index = 1, text = CURSOR_INPUT_TEXT)

    inputSame(compareText = CURSOR_INPUT)
    rule.cursorInputNode().exist()
    inputSame(compareText = CURSOR_INPUT)

    textField().textClear(TEST_INPUT)

    initState()
}

fun textFieldVisibleTest(
    rule: CustomTestRule
) {
    fun visibleIconExist() {
        rule.visibleIcon.exist()
        rule.invisibleIcon.notExist()
    }

    fun invisibleIconExist() {
        rule.visibleIcon.notExist()
        rule.invisibleIcon.exist()
    }

    fun invisibleTextNode() = rule.getNodeWithText(rule.invisibleText(TEST_INPUT.length))

    visibleIconExist()
    rule.testInputNode().notExist()

    rule.passwordTextField.invisibleInput(TEST_INPUT)
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
    fun cancelIconNotExist() =
        rule.restore {
            rule.cancelIcon.notExist(false)
            noCancelIconTextField().click() // loose focus
            rule.cancelIcon.notExist()
            cancelIconTextField().click() // get focus
            rule.cancelIcon.notExist(false)
        }

    fun cancelIconExist() =
        rule.restore {
            cancelIconTextField().click()
            rule.cancelIcon.exist(false)
            noCancelIconTextField().click()
            rule.cancelIcon.notExist(false)
        }

    cancelIconNotExist() // 초기 화면

    cancelIconTextField().input(TEST_INPUT) // 인풋 입력

    cancelIconTextField().click() // get focus(인풋 변경 시 포커스 사라짐)
    rule.cancelIcon.exist(false) // 캔슬 아이콘 생성
    rule.restore() // 액티비티 재구성
    rule.cancelIcon.notExist(false) // 캔슬 아이콘 사라짐(loose focus)
    cancelIconExist() // 캔슬 아이콘 유지 확인

    cancelIconTextField().click() // 캔슬 아이콘 생성(get focus)
    rule.cancelIcon.click() // 캔슬 아이콘 기능 확인

    rule.restore {
        rule.testInputNode().notExist(false) // 인풋 사라짐 확인
        cancelIconTextField().click() // get focus
        rule.cancelIcon.notExist(false) // 캔슬 아이콘 사라짐 확인
        noCancelIconTextField().click() // loose focus
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
    blockBlank: Boolean = false
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

    errorTextField().input(TEST_INPUT)
    errorNotExist()
}

fun cursorTest(
    rule: CustomTestRule,
    textField1: () -> CustomSemanticsNodeInteraction,
    textField2: () -> CustomSemanticsNodeInteraction
) {
    fun changeFocus() {
        textField2().click()
        textField1().notFocused()
        textField2().focused()

        textField1().click()
        textField1().focused()
        textField2().notFocused()
    }

    textField1().input(TEST_INPUT)
    changeFocus()
    textField1().inputWithNoRestore(TEST_INPUT.length, CURSOR_INPUT_TEXT)

    rule.getNodeWithText(TEST_INPUT + CURSOR_INPUT_TEXT).exist()

    repeat(2) { changeFocus() }
    textField1().inputWithNoRestore((TEST_INPUT + CURSOR_INPUT_TEXT).length, CURSOR_INPUT_TEXT)
    rule.getNodeWithText(TEST_INPUT + CURSOR_INPUT_TEXT + CURSOR_INPUT_TEXT).exist()
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
    textField: () -> CustomSemanticsNodeInteraction
) {
    textField().input(TEST_INPUT)

    textField().inputWithNoRestore(0, " ")
    rule.getNodeWithText("$TEST_INPUT ").notExist()
    textField().inputWithNoRestore(6, " ")
    rule.getNodeWithText(" $TEST_INPUT").notExist()
}

private fun CustomTestRule.testInputNode() = getNodeWithText(TEST_INPUT)
private fun CustomTestRule.cursorInputNode() = getNodeWithText(CURSOR_INPUT)