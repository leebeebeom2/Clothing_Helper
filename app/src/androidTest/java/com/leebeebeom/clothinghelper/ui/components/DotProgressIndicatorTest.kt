package com.leebeebeom.clothinghelper.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Rule
import org.junit.Test

class DotProgressIndicatorTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun centerDotProgressIndicatorTest() {
        var isLoading by mutableStateOf(false)
        rule.setContent {
            ClothingHelperTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CenterDotProgressIndicator { isLoading }
                }
            }
        }
        rule.onNodeWithTag(CENTER_DOT_PROGRESS_INDICATOR_TAG).assertDoesNotExist()
        isLoading = true
        rule.onNodeWithTag(CENTER_DOT_PROGRESS_INDICATOR_TAG).assertExists()
        isLoading = false
        rule.onNodeWithTag(CENTER_DOT_PROGRESS_INDICATOR_TAG).assertDoesNotExist()
    }
}