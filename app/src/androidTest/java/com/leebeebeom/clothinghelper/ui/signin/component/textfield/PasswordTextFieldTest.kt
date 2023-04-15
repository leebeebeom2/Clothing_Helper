package com.leebeebeom.clothinghelper.ui.signin.component.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.input.ImeAction
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.getInvisibleText
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.component.textfield.TestText
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PasswordTextFieldTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val passwordTextField = rule.onNodeWithStringRes(R.string.password)
    private val visibleIcon = rule.onNodeWithTag(VisibleIconTag)
    private val invisibleIcon = rule.onNodeWithTag(InvisibleIconTag)

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
        passwordTextField.assert(hasText(getInvisibleText(TestText.length)))

        visibleIcon.performClick()

        visibleIcon.assertDoesNotExist()
        invisibleIcon.assertExists()
        passwordTextField.assert(hasText(TestText))

        invisibleIcon.performClick()
        visibleIcon.assertExists()
        invisibleIcon.assertDoesNotExist()
        passwordTextField.assert(hasText(getInvisibleText(TestText.length)))
    }
}