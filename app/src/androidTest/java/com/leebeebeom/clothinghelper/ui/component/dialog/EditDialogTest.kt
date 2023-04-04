package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.onNodeWithStringRes
import kotlinx.collections.immutable.toImmutableSet
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditDialogTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val names = List(10) { "$it" }.toImmutableSet()
    private val initialName = "이니셜 네임"
    private var show by mutableStateOf(false)
    private var positiveButtonClicked = false
    private val textField by lazy { rule.onNodeWithStringRes(R.string.test_text_field) }
    private val checkButton by lazy { rule.onNodeWithStringRes(R.string.check) }

    @Before
    fun init() {
        restorationTester.setContent {
            EditNameDialog(
                label = R.string.test_text_field,
                placeHolder = null,
                title = R.string.test_dialog_title,
                existNameError = R.string.error_exist_category_name,
                names = { names },
                onPositiveButtonClick = { positiveButtonClicked = true },
                onDismiss = { show = false },
                initialName = initialName
            )
        }
    }

    @Test
    fun editDialogErrorTest() {
        rule.onNodeWithText(initialName).assertExists()
        textField.performTextInput("1")
        rule.onNodeWithStringRes(R.string.error_exist_category_name).assertExists()
        checkButton.assertIsNotEnabled()

        restorationTester.emulateSavedInstanceStateRestore()

        rule.onNodeWithText("1")
        rule.onNodeWithStringRes(R.string.error_exist_category_name).assertExists()
        checkButton.assertIsNotEnabled()

        textField.performTextInput("11")
        rule.onNodeWithStringRes(R.string.error_exist_category_name).assertDoesNotExist()
        checkButton.assertIsEnabled()

        restorationTester.emulateSavedInstanceStateRestore()

        rule.onNodeWithText("11")
        rule.onNodeWithStringRes(R.string.error_exist_category_name).assertDoesNotExist()
        checkButton.assertIsEnabled()
    }
}