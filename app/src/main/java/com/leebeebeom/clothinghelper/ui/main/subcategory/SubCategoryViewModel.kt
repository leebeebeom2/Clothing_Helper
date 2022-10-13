package com.leebeebeom.clothinghelper.ui.main.subcategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState

class SubCategoryViewModel : ViewModel() {
    var subCategoryUIState by mutableStateOf(SubCategoryUIState())
        private set

    val showAddCategoryDialog = { subCategoryUIState = subCategoryUIState.onShowAddCategoryDialog() }

    val onDismissAddCategoryDialog =
        { subCategoryUIState = subCategoryUIState.onDismissAddCategoryDialog() }

    val onNewCategoryNameChange = { newCategoryName: String ->
        subCategoryUIState = subCategoryUIState.onNewCategoryNameChange(newCategoryName)

        if (subCategoryUIState.subCategories.contains(newCategoryName))
            subCategoryUIState =
                subCategoryUIState.setCategoryNameError(TextFieldError.ERROR_SAME_CATEGORY_NAME)
    }

    fun addNewCategory() {
        subCategoryUIState = subCategoryUIState.addNewCategory()
    }
}

data class SubCategoryUIState(
    val showDialog: Boolean = false,
    val subCategories: List<String> = getInitialSubCategories(),
    val categoryName: TextFieldUIState = TextFieldUIState()
) {
    val positiveButtonEnable get() = !categoryName.isBlank && !categoryName.isError

    fun onShowAddCategoryDialog() = copy(showDialog = true)

    fun onDismissAddCategoryDialog() = copy(
        showDialog = false,
        categoryName = categoryName.textInit().errorOff()
    )

    fun onNewCategoryNameChange(newCategory: String) =
        copy(categoryName = categoryName.textChangeAndErrorOff(newCategory))

    fun setCategoryNameError(error: TextFieldError) =
        copy(categoryName = categoryName.errorOn(error))

    fun addNewCategory(): SubCategoryUIState {
        val list = subCategories.toMutableList()
        list.add(categoryName.text)
        return copy(subCategories = list)
    }
}

// TODO 삭제
fun getInitialSubCategories() =
    mutableListOf("반팔 티셔츠", "긴팔 티셔츠", "셔츠", "반팔 셔츠", "니트", "반팔 니트", "니트 베스트")