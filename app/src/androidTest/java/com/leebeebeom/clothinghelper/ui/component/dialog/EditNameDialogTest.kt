package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performTextInput
import com.leebeebeom.clothinghelper.R.string.error_exist_folder_name
import com.leebeebeom.clothinghelper.R.string.positive
import com.leebeebeom.clothinghelper.R.string.test_dialog_title
import com.leebeebeom.clothinghelper.R.string.test_text_field
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.component.dialog.component.DialogMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.component.dialog.component.MutableTextFieldDialogState
import com.leebeebeom.clothinghelper.ui.component.dialog.component.rememberDialogMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.component.dialog.component.rememberTextFieldDialogState
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import kotlinx.collections.immutable.toImmutableSet
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditNameDialogTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private var show by mutableStateOf(false)
    private val names = List(5) { "$it" }.toImmutableSet()
    private lateinit var initialName: String
    private lateinit var state: MutableTextFieldDialogState
    private lateinit var textFieldState: DialogMaxWidthTextFieldState

    private val textField = rule.onNodeWithStringRes(test_text_field)
    private val positiveButton = rule.onNodeWithStringRes(positive)
    private val error = rule.onNodeWithStringRes(error_exist_folder_name)

    @Before
    fun init() {
        initialName = "초기 이름"

        restorationTester.setContent {
            state = rememberTextFieldDialogState(initialText = initialName)
            textFieldState = rememberDialogMaxWidthTextFieldState(initialText = state.initialText)

            ClothingHelperTheme {
                if (show)
                    EditNameDialog(
                        label = test_text_field,
                        placeHolder = null,
                        title = test_dialog_title,
                        existNameError = error_exist_folder_name,
                        names = { names },
                        onPositiveButtonClick = { },
                        onDismiss = { show = false },
                        initialName = { initialName },
                        state = state,
                        dialogMaxWidthTextFieldState = textFieldState
                    )
            }
        }
    }

    @Test
    fun editNameDialogErrorTest() {
        show = true

        textField.assert(hasText(initialName))
        error.assertDoesNotExist()
        positiveButton.assertIsNotEnabled()

        textField.performTextInput("1")
        error.assertExists()

        textField.performTextInput("6")
        error.assertDoesNotExist()

        textField.performTextInput(initialName)
        error.assertDoesNotExist()
    }
}