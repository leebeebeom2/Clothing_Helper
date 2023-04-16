package com.leebeebeom.clothinghelper.ui.component.dialog.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.TextRange
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TextFieldDialogTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private var show by mutableStateOf(true)
    private var error: Int? by mutableStateOf(null)
    private val initialText = "초기 텍스트"
    private val textField = rule.onNodeWithStringRes(R.string.test_text_field)
    private val positiveButton = rule.onNodeWithStringRes(R.string.positive)
    private val cancelButton = rule.onNodeWithStringRes(R.string.cancel)
    private val dialogTitle = rule.onNodeWithStringRes(R.string.test_dialog_title)
    private val errorNode = rule.onNodeWithStringRes(R.string.test_error_msg)
    private lateinit var state: MutableTextFieldDialogState
    private lateinit var dialogMaxWidthTextFieldState: DialogMaxWidthTextFieldState

    @Before
    fun init() {
        restorationTester.setContent {
            ClothingHelperTheme {
                if (show) {
                    state = rememberTextFieldDialogState(initialText = initialText)
                    dialogMaxWidthTextFieldState =
                        rememberDialogMaxWidthTextFieldState(initialText = initialText)
                    TextFieldDialog(
                        state = state,
                        dialogMaxWidthTextFieldState = dialogMaxWidthTextFieldState,
                        label = R.string.test_text_field,
                        placeHolder = null,
                        title = R.string.test_dialog_title,
                        error = { error },
                        onPositiveButtonClick = { },
                        onDismiss = { show = false },
                        onInputChange = state::onInputChange
                    )
                }
            }
        }
    }

    @Test
    fun positiveButtonTest() {
        positiveButton.assertIsNotEnabled()

        textField.performTextInput("1")
        positiveButton.assertIsEnabled()

        textField.performTextClearance()
        positiveButton.assertIsNotEnabled()

        textField.performTextInput("1")
        positiveButton.assertIsEnabled()

        error = R.string.test_error_msg
        positiveButton.assertIsNotEnabled()
    }

    @Test
    fun textFieldButtonTest() {
        textField.performTextInput("1")
        positiveButton.performClick()

        dialogTitle.assertDoesNotExist()

        assert(!show)
        show = true

        cancelButton.performClick()
        dialogTitle.assertDoesNotExist()
    }

    @Test
    fun textFieldDialogTextRangeTest() {
        assert(
            dialogMaxWidthTextFieldState.textFieldValue.selection ==
                    TextRange(0, initialText.length)
        )

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
        errorNode.assertExists()

        repeat(2) {
            textField.assert(hasText("테스트"))
            errorNode.assertExists()
            positiveButton.assertIsNotEnabled()

            restorationTester.emulateSavedInstanceStateRestore()
        }

        textField.performTextInput("1")
        error = null
        errorNode.assertDoesNotExist()

        repeat(2) {
            textField.assert(hasText("1"))
            errorNode.assertDoesNotExist()
            positiveButton.assertIsEnabled()

            restorationTester.emulateSavedInstanceStateRestore()
        }
    }
}