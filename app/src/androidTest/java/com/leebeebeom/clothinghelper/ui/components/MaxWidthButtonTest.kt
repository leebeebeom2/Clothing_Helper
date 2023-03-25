package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.onNodeWithStringRes
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MaxWidthButtonTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private var onClick = ""
    private var enabled by mutableStateOf(true)
    private val testTextField by lazy { rule.onNodeWithStringRes(R.string.test_text_field) }
    private val button by lazy { rule.onNodeWithStringRes(R.string.test_button) }

    @Before
    fun init() {
        onClick = ""
        rule.setContent {
            Column(modifier = Modifier.fillMaxSize()) {
                StatefulMaxWidthTestField(label = R.string.test_text_field)
                MaxWidthButton(text = R.string.test_button, enabled = { enabled }) {
                    onClick = "onClick"
                }
            }
        }
    }

    @Test
    fun buttonFocusTest() {
        testTextField.performClick()
        testTextField.assertIsFocused()

        button.performClick()
        testTextField.assertIsNotFocused()
    }

    @Test
    fun onClickTest() {
        assert(onClick == "")
        button.performClick()
        assert(onClick == "onClick")
    }

    @Test
    fun enabledTest() {
        enabled = false
        button.assertIsNotEnabled()
        enabled = true
        button.assertIsEnabled()
    }
}