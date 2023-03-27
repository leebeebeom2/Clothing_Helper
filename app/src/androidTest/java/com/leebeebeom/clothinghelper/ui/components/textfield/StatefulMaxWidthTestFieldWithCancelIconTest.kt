package com.leebeebeom.clothinghelper.ui.components.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.components.CancelIconTag
import com.leebeebeom.clothinghelper.ui.components.StatefulMaxWidthTestField
import com.leebeebeom.clothinghelper.ui.components.StatefulMaxWidthTestFieldWithCancelIcon
import com.leebeebeom.clothinghelper.ui.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StatefulMaxWidthTestFieldWithCancelIconTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val restorationTester = StateRestorationTester(rule)
    private val testTextField by lazy { rule.onNodeWithStringRes(R.string.test_text_field) }
    private val testTextField2 by lazy { rule.onNodeWithStringRes(R.string.test_text_field2) }
    private var input = ""

    @Before
    fun init() {
        restorationTester.setContent {
            ClothingHelperTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    StatefulMaxWidthTestFieldWithCancelIcon(
                        label = R.string.test_text_field,
                        onInputChange = { input = it })
                    StatefulMaxWidthTestField(label = R.string.test_text_field2)
                }
            }
        }
    }

    @Test
    fun cancelIconTest() {
        rule.onNodeWithTag(CancelIconTag).assertDoesNotExist()

        testTextField.performClick()
        rule.onNodeWithTag(CancelIconTag).assertDoesNotExist()

        testTextField.performTextInput(TestText)
        assert(input == TestText)
        rule.onNodeWithTag(CancelIconTag).assertExists()

        testTextField2.performClick()
        rule.onNodeWithTag(CancelIconTag).assertDoesNotExist()

        testTextField.performClick()
        rule.onNodeWithTag(CancelIconTag).assertExists()

        rule.onNodeWithTag(CancelIconTag).performClick()
        rule.onNodeWithText(TestText).assertDoesNotExist()
        assert(input == "")
        rule.onNodeWithTag(CancelIconTag).assertDoesNotExist()
    }
}