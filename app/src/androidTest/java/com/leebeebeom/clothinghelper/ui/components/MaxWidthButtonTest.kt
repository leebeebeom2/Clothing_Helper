package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.leebeebeom.clothinghelper.CustomTestRule
import com.leebeebeom.clothinghelper.R.string.check
import com.leebeebeom.clothinghelper.R.string.dummy
import com.leebeebeom.clothinghelper.activityRule
import com.leebeebeom.clothinghelper.checkButton
import com.leebeebeom.clothinghelper.dummyNode
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MaxWidthButtonTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule = rule)
    private val testText = "onClick"
    private lateinit var onClickTestText: String
    private lateinit var enable: MutableState<Boolean>

    private val checkButton get() = checkButton(customTestRule)
    private val dummyNode get() = dummyNode(customTestRule)

    @Before
    fun init() {
        enable = mutableStateOf(true)
        onClickTestText = ""

        customTestRule.setContent {
            ClothingHelperTheme {
                Column {
                    MaxWidthTextFieldWithError(
                        state = rememberMaxWidthTextFieldState(label = dummy),
                        onValueChange = {},
                        onFocusChanged = {},
                        onInputChange = {}
                    )
                    MaxWidthButton(text = check,
                        enabled = { enable.value },
                        onClick = { onClickTestText = testText })
                }
            }
        }
    }

    @Test
    fun buttonFocusTest() =
        buttonFocusTest(focusNode = { dummyNode }, button = { checkButton })

    @Test
    fun buttonEnabledTest() = buttonEnableTest(button = { checkButton },
        initialEnabled = enable.value,
        setEnable = { enable.value = it })

    @Test
    fun onClickTest() = customTestRule.restore {
        checkButton.click()
        assert(onClickTestText == testText)

        onClickTestText = ""
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

    setEnable(!initialEnabled)

    enableCheck(!initialEnabled)
}