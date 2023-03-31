package com.leebeebeom.clothinghelper.ui.component.dialog.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextRange
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.onNodeWithStringRes
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TextFieldDialogTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private var show by mutableStateOf(true)
    private var error: Int? by mutableStateOf(null)
    private var positiveButtonClicked = false
    private val textField by lazy { rule.onNodeWithStringRes(R.string.test_text_field) }
    private val checkButton by lazy { rule.onNodeWithStringRes(R.string.check) }
    private val cancelButton by lazy { rule.onNodeWithStringRes(R.string.cancel) }
    private lateinit var state: MutableTextFieldDialogState
    private lateinit var dialogMaxWidthTextFieldState: DialogMaxWidthTextFieldState

    @Before
    fun init() {
        restorationTester.setContent {
            if (show) {
                state = rememberTextFieldDialogState(initialText = "")
                dialogMaxWidthTextFieldState =
                    rememberDialogMaxWidthTextFieldState(initialText = "")
                TextFieldDialog(
                    state = state,
                    dialogMaxWidthTextFieldState = dialogMaxWidthTextFieldState,
                    label = R.string.test_text_field,
                    placeHolder = null,
                    title = R.string.test_dialog_title,
                    error = { error },
                    onPositiveButtonClick = { positiveButtonClicked = true },
                    onDismiss = { show = false },
                    onInputChange = state::onInputChange
                )
            }
        }
    }

    // TODO restoreTest <- 여기

    @Test
    fun textFieldDialogErrorTest() {
        textField.performTextInput("테스트")
        checkButton.assertIsEnabled()

        error = R.string.test_error_msg
        rule.onNodeWithStringRes(R.string.test_error_msg).assertExists()
        checkButton.assertIsNotEnabled()

        error = null
        rule.onNodeWithStringRes(R.string.test_error_msg).assertDoesNotExist()
        checkButton.assertIsEnabled()

        textField.performTextClearance()
        checkButton.assertIsNotEnabled()
    }

    @Test
    fun textFieldDialogButtonTest() {
        cancelButton.performClick()
        rule.onNodeWithStringRes(R.string.test_dialog_title).assertDoesNotExist()
        assert(!show)

        show = true

        rule.onNodeWithStringRes(R.string.test_dialog_title).assertExists()
        textField.performTextInput("테스트")
        checkButton.performClick()
        rule.onNodeWithStringRes(R.string.test_dialog_title).assertDoesNotExist()
        assert(positiveButtonClicked)

        positiveButtonClicked = false
    }

    @Test
    fun textFieldDialogTextRangeTest() {
        textField.performTextInput("테스트")
        textField.performImeAction()
        textField.assertIsNotFocused()

        textField.performClick()
        assert(
            dialogMaxWidthTextFieldState.textFieldValue.selection == TextRange(0, "테스트".length)
        )
    }

    @Test
    fun restoreTest() {
        textField.performTextInput("테스트")
        error = R.string.test_error_msg
        rule.onNodeWithStringRes(R.string.test_error_msg).assertExists()
        checkButton.assertIsNotEnabled()

        restorationTester.emulateSavedInstanceStateRestore()

        rule.onNodeWithText("테스트").assertExists()
        rule.onNodeWithStringRes(R.string.test_error_msg).assertExists()
        checkButton.assertIsNotEnabled()

        textField.performTextInput("1")
        error = null
        rule.onNodeWithStringRes(R.string.test_error_msg).assertDoesNotExist()
        checkButton.assertIsEnabled()

        restorationTester.emulateSavedInstanceStateRestore()

        rule.onNodeWithText("테스트1")
        rule.onNodeWithStringRes(R.string.test_error_msg).assertDoesNotExist()
        checkButton.assertIsEnabled()
    }
}