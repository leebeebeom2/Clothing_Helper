package com.leebeebeom.clothinghelper.ui.component.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.component.CancelIconTag
import com.leebeebeom.clothinghelper.ui.component.StatefulMaxWidthTextField
import com.leebeebeom.clothinghelper.ui.component.StatefulMaxWidthTextFieldWithCancelIcon
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StatefulMaxWidthTestFieldWithCancelIconTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val testTextField = rule.onNodeWithStringRes(R.string.test_text_field)
    private val testTextField2 = rule.onNodeWithStringRes(R.string.test_text_field2)
    private val cancelIcon = rule.onNodeWithTag(CancelIconTag)
    private var input = ""

    @Before
    fun init() {
        restorationTester.setContent {
            ClothingHelperTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    StatefulMaxWidthTextFieldWithCancelIcon(
                        label = R.string.test_text_field,
                        onInputChange = { input = it })
                    StatefulMaxWidthTextField(label = R.string.test_text_field2)
                }
            }
        }
    }

    @Test
    fun cancelIconTest() {
        cancelIcon.assertDoesNotExist()

        testTextField.performClick()
        cancelIcon.assertDoesNotExist()

        testTextField.performTextInput(TestText)
        assert(input == TestText)
        cancelIcon.assertExists()

        testTextField2.performClick()
        cancelIcon.assertDoesNotExist()

        testTextField.performClick()
        cancelIcon.assertExists()

        cancelIcon.performClick()
        testTextField.assert(!hasText(TestText))
        assert(input == "")
        cancelIcon.assertDoesNotExist()
    }
}