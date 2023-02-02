package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.performClick
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.R.string.check
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
            Column {
                EmailTextField(error = { null }, onInputChange = {})
                MaxWidthButton(text = check,
                    enabled = { enable.value },
                    onClick = { onClickTest = onClickTestText })
            }
        }
    }

    @Test
    fun buttonFocusTest() = buttonFocusTest(
        focusNode = rule.emailTextField,
        button = rule.checkButton,
        restoreTester = restoreTester
    )

    @Test
    fun buttonEnabledTest() = buttonEnabledTest(
        button = rule.checkButton,
        setEnable = { enable.value = it },
        restoreTester = restoreTester
    )

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