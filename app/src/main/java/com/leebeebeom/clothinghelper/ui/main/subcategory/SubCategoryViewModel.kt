package com.leebeebeom.clothinghelper.ui.main.subcategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState

class SubCategoryViewModel : ViewModel() {
    var addDialogState by mutableStateOf(AddDialogState())

    val onShowAddCategoryDialog = { update(addDialogState.copy(isDialogShowing = true)) }

    val onDismissAddCategoryDialog = {
        update(
            addDialogState.copy(
                isDialogShowing = false,
                categoryTextFieldState = addDialogState.categoryTextFieldState
                    .textInit()
                    .errorOff()
            )
        )
    }

    val onNewCategoryNameChange = { newCategoryName: String ->
        update(
            addDialogState.copy(
                categoryTextFieldState = addDialogState.categoryTextFieldState
                    .textChangeAndErrorOff(newCategoryName)
            )
        )

        if (addDialogState.subCategories.contains(newCategoryName))
            update(
                addDialogState.copy(
                    categoryTextFieldState = addDialogState.categoryTextFieldState
                        .errorOn(TextFieldError.ERROR_SAME_CATEGORY_NAME)
                )
            )
    }

    fun addNewCategory() {
        val newList = addDialogState.subCategories.toMutableList()
        newList.add(addDialogState.categoryTextFieldState.text)
        update(addDialogState.copy(subCategories = newList))
    }

    private fun update(newState: AddDialogState) {
        addDialogState = newState
    }
}

data class AddDialogState(
    val isDialogShowing: Boolean = false,
    val subCategories: List<String> = getInitialSubCategories(),
    val categoryTextFieldState: TextFieldUIState = TextFieldUIState()
){
    val positiveButtonEnable get() = !categoryTextFieldState.isBlank && !categoryTextFieldState.isError
}

// TODO 삭제
fun getInitialSubCategories() =
    mutableListOf("반팔 티셔츠", "긴팔 티셔츠", "셔츠", "반팔 셔츠", "니트", "반팔 니트", "니트 베스트")