package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.EmailTextField
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MaxWidthButtonTest {
    @get:Rule
    val rule = composeRule
    private val restoreTester = restoreTester(rule)
    private val onClickTestText = "onClick"
    private lateinit var onClickTest: String
    private lateinit var enable: MutableState<Boolean>

    @Before
    fun init() {
        enable = mutableStateOf(true)
        onClickTest = ""

        restoreTester.setContent {
            EmailTextField(error = { null }, onInputChange = {})
            MaxWidthButton(text = R.string.check,
                enabled = { enable.value },
                onClick = { onClickTest = onClickTestText })
        }
    }

    @Test
    fun focusTest() {
        rule.emailTextField.assertIsFocused()
        rule.checkButton.performClick()
        rule.emailTextField.assertIsNotFocused()
    }

    @Test
    fun enabledTest() {
        rule.checkButton.assertIsEnabled()
        restoreTester.emulateSavedInstanceStateRestore()
        rule.checkButton.assertIsEnabled()

        enable.value = false

        rule.checkButton.assertIsNotEnabled()
        restoreTester.emulateSavedInstanceStateRestore()
        rule.checkButton.assertIsNotEnabled()

        enable.value = true

        rule.checkButton.assertIsEnabled()
        restoreTester.emulateSavedInstanceStateRestore()
        rule.checkButton.assertIsEnabled()
    }

    @Test
    fun onClickTest() {
        rule.checkButton.performClick()
        assert(onClickTest == onClickTestText)
        onClickTest = ""

        restoreTester.emulateSavedInstanceStateRestore()
        rule.checkButton.performClick()
        assert(onClickTest == onClickTestText)
    }
}