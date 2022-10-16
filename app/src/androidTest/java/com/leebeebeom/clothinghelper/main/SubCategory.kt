package com.leebeebeom.clothinghelper.main

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class SubCategory {
    @get:Rule
    val subCategoryRule = createComposeRule()

    @Test
    fun addCategoryDialog() {
        subCategoryRule.onNodeWithText("확인").assertIsNotEnabled()

        val categoryTextField = getCategoryTextField()

        categoryTextField.performTextInput("셔츠")
        subCategoryRule.onNodeWithText("이미 존재하는 카테고리 입니다.").assertExists()
        categoryTextField.performTextClearance()
        subCategoryRule.onNodeWithText("확인").assertIsNotEnabled()
    }

    private fun getCategoryTextField() = subCategoryRule.onNodeWithText("카테고리")
}