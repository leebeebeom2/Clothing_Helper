package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Rule
import org.junit.Test

class DotProgressIndicatorTest {
    @get:Rule
    val rule = createComposeRule()
    private val restoreTester = StateRestorationTester(rule)

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
        assertDoesNotExists()

        isLoading = true

        assertExists()
        restoreTester.emulateSavedInstanceStateRestore()
        assertExists()

        isLoading = false

        assertDoesNotExists()
        restoreTester.emulateSavedInstanceStateRestore()
        assertDoesNotExists()
    }

    private fun assertExists() =
        rule.onNodeWithTag(CENTER_DOT_PROGRESS_INDICATOR_TAG).assertExists()

    private fun assertDoesNotExists() =
        rule.onNodeWithTag(CENTER_DOT_PROGRESS_INDICATOR_TAG).assertDoesNotExist()
}