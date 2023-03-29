package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.components.textfield.TestText
import com.leebeebeom.clothinghelper.ui.getInvisibleText
import com.leebeebeom.clothinghelper.ui.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PasswordTextFieldTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val passwordTextField by lazy { rule.onNodeWithStringRes(R.string.password) }
    private val visibleIcon by lazy { rule.onNodeWithTag(VisibleIconTag) }
    private val invisibleIcon by lazy { rule.onNodeWithTag(InvisibleIconTag) }

    @Before
    fun init() {
        rule.setContent {
            ClothingHelperTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    PasswordTextField(
                        error = { null },
                        imeAction = ImeAction.Done,
                        onInputChange = {}
                    )
                }
            }
        }
    }

    @Test
    fun visibleTest() {
        visibleIcon.assertExists()

        passwordTextField.performTextInput(TestText)
        rule.onNodeWithText(getInvisibleText(TestText.length)).assertExists()

        visibleIcon.performClick()

        visibleIcon.assertDoesNotExist()
        invisibleIcon.assertExists()
        rule.onNodeWithText(TestText).assertExists()

        invisibleIcon.performClick()
        visibleIcon.assertExists()
        invisibleIcon.assertDoesNotExist()
        rule.onNodeWithText(getInvisibleText(TestText.length)).assertExists()
    }
}