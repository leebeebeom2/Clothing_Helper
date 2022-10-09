package com.leebeebeom.clothinghelper.main

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.ui.main.subcategory.AddCategoryDialog
import com.leebeebeom.clothinghelper.ui.main.subcategory.SubCategoryViewModel
import org.junit.Rule
import org.junit.Test

class SubCategory {
    @get:Rule
    val subCategoryRule = createComposeRule()

    @Test
    fun addCategoryDialog() {
        subCategoryRule.setContent {
            val viewModel: SubCategoryViewModel = viewModel()
            val dialogState = viewModel.addDialogState

            AddCategoryDialog(
                onDismissDialog = viewModel.onDismissAddCategoryDialog,
                categoryTextFieldState = dialogState.categoryTextFieldState,
                onNewCategoryNameChange = viewModel.onNewCategoryNameChange,
                positiveButtonEnabled = dialogState.positiveButtonEnable,
                onCancelButtonClick = viewModel.onDismissAddCategoryDialog,
                onPositiveButtonClick = {
                    viewModel.addNewCategory()
                    viewModel.onDismissAddCategoryDialog()
                },
            )
        }

        subCategoryRule.onNodeWithText("확인").assertIsNotEnabled()

        val categoryTextField = getCategoryTextField()

        categoryTextField.performTextInput("셔츠")
        subCategoryRule.onNodeWithText("이미 존재하는 카테고리 입니다.").assertExists()
        categoryTextField.performTextClearance()
        subCategoryRule.onNodeWithText("확인").assertIsNotEnabled()
    }

    private fun getCategoryTextField() = subCategoryRule.onNodeWithText("카테고리")
}