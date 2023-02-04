package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Rule
import org.junit.Test

class DotProgressIndicatorTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule = rule)

    @Test
    fun centerDotProgressIndicatorTest() {
        var isLoading by mutableStateOf(false)

        customTestRule.setContent {
            ClothingHelperTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CenterDotProgressIndicator { isLoading }
                }
            }
        }

        customTestRule.centerDotProgressIndicator.notExist()

        isLoading = true

        customTestRule.centerDotProgressIndicator.exist()

        isLoading = false

        customTestRule.centerDotProgressIndicator.notExist()
    }
}