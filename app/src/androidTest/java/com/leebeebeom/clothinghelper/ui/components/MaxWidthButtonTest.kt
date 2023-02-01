package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.EmailTextField
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MaxWidthButtonTest {
    @get:Rule
    val rule = createComposeRule()
    lateinit var restoreTester: StateRestorationTester
    private val onClickTestText = "onClick"
    private var onClickTest = ""
    private lateinit var enable: MutableState<Boolean>

    @Before
    fun init() {
        enable = mutableStateOf(true)
        onClickTest = ""

        restoreTester = StateRestorationTester(rule)
        restoreTester.setContent {
            EmailTextField(error = { null }, onInputChange = {})
            MaxWidthButton(text = R.string.check,
                enabled = { enable.value },
                onClick = { onClickTest = onClickTestText })
        }
    }

    @Test
    fun focusTest() {
        textField.assertIsFocused()
        button.performClick()
        textField.assertIsNotFocused()
    }

    @Test
    fun enabledTest() {
        button.assertIsEnabled()
        restoreTester.emulateSavedInstanceStateRestore()
        button.assertIsEnabled()

        enable.value = false

        button.assertIsNotEnabled()
        restoreTester.emulateSavedInstanceStateRestore()
        button.assertIsNotEnabled()

        enable.value = true

        button.assertIsEnabled()
        restoreTester.emulateSavedInstanceStateRestore()
        button.assertIsEnabled()
    }

    @Test
    fun onClickTest() {
        button.performClick()
        assert(onClickTest == onClickTestText)
        onClickTest = ""

        restoreTester.emulateSavedInstanceStateRestore()
        button.performClick()
        assert(onClickTest == onClickTestText)
    }

    private val textField get() = rule.onNodeWithText("이메일")
    private val button get() = rule.onNodeWithText("확인")
}