package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.leebeebeom.clothinghelper.CustomTestRule
import com.leebeebeom.clothinghelper.R.string.check
import com.leebeebeom.clothinghelper.activityRule
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.EmailTextField
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MaxWidthButtonTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule = rule)
    private val onClickTestText = "onClick"
    private lateinit var onClickTest: String
    private lateinit var enable: MutableState<Boolean>

    @Before
    fun init() {
        enable = mutableStateOf(true)
        onClickTest = ""

        customTestRule.setContent {
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
        focusNode = { customTestRule.emailTextField },
        button = { customTestRule.checkButton }
    )

    @Test
    fun buttonEnabledTest() = buttonEnableTest(
        button = { customTestRule.checkButton },
        initialEnabled = true,
        setEnable = { enable.value = it }
    )

    @Test
    fun onClickTest() =
        customTestRule.restore {
            customTestRule.checkButton.click()
            assert(onClickTest == onClickTestText)

            onClickTest = ""
        }
}

fun buttonFocusTest(
    focusNode: () -> CustomTestRule.CustomSemanticsNodeInteraction,
    button: () -> CustomTestRule.CustomSemanticsNodeInteraction
) {
    focusNode().click()
    focusNode().focused()
    button().click()
    focusNode().notFocused()
}

fun buttonEnableTest(
    button: () -> CustomTestRule.CustomSemanticsNodeInteraction,
    initialEnabled: Boolean,
    setEnable: (Boolean) -> Unit,
) {
    fun enableCheck(initialEnabled: Boolean) =
        if (initialEnabled) button().enabled() else button().notEnabled()

    enableCheck(initialEnabled)

    setEnable(!initialEnabled)

    enableCheck(!initialEnabled)

    setEnable(initialEnabled)

    enableCheck(initialEnabled)
}