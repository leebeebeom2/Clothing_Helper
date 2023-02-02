package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.centerDotProgressIndicator
import com.leebeebeom.clothinghelper.composeRule
import com.leebeebeom.clothinghelper.restoreTester
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Rule
import org.junit.Test

class DotProgressIndicatorTest {
    @get:Rule
    val rule = composeRule
    private val restoreTester = restoreTester(rule)

    @Test
    fun centerDotProgressIndicatorTest() {
        var isLoading by mutableStateOf(false)

        restoreTester.setContent {
            ClothingHelperTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CenterDotProgressIndicator { isLoading }
                }
            }
        }
        rule.centerDotProgressIndicator.assertDoesNotExist()

        isLoading = true

        rule.centerDotProgressIndicator.assertExists()
        restoreTester.emulateSavedInstanceStateRestore()
        rule.centerDotProgressIndicator.assertExists()

        isLoading = false

        rule.centerDotProgressIndicator.assertDoesNotExist()
        restoreTester.emulateSavedInstanceStateRestore()
        rule.centerDotProgressIndicator.assertDoesNotExist()
    }
}