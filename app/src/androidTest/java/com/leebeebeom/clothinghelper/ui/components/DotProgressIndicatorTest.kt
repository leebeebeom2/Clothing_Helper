package com.leebeebeom.clothinghelper.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Rule
import org.junit.Test

class DotProgressIndicatorTest {
    private val centerDotProgressIndicator = "center dot progress indicator"

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun centerDotProgressIndicatorTest() {
        var isLoading by mutableStateOf(false)
        rule.setContent {
            ClothingHelperTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CenterDotProgressIndicator(
                        modifier = Modifier.testTag(centerDotProgressIndicator)
                    ) { isLoading }
                }
            }
        }
        rule.onNodeWithTag(centerDotProgressIndicator).assertDoesNotExist()
        isLoading = true
        rule.onNodeWithTag(centerDotProgressIndicator).assertExists()
        isLoading = false
        rule.onNodeWithTag(centerDotProgressIndicator).assertDoesNotExist()
    }
}