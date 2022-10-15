package com.leebeebeom.clothinghelper.ui.main.subcategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState

class SubCategoryViewModel : ViewModel() {
    var subCategoryUIState by mutableStateOf(SubCategoryUIState())
        private set

    val showAddCategoryDialog =
        { subCategoryUIState = subCategoryUIState.copy(showDialog = true) }

    val onDismissAddCategoryDialog = {
        subCategoryUIState.categoryName.text = ""
        subCategoryUIState = subCategoryUIState.copy(showDialog = false)
    }

    val onNewCategoryNameChange = { newCategoryName: String ->
        if (subCategoryUIState.subCategories.contains(newCategoryName))
            subCategoryUIState.categoryName.error = TextFieldError.ERROR_SAME_CATEGORY_NAME
    }
}

data class SubCategoryUIState(
    val showDialog: Boolean = false,
    val subCategories: SnapshotStateList<String> = getInitialSubCategories(),
    val categoryName: TextFieldUIState = TextFieldUIState(
        label = R.string.category,
        placeHolder = R.string.category_place_holder
    )
) {
    val positiveButtonEnabled get() = !categoryName.isBlank && !categoryName.isError

    fun addNewCategory() = subCategories.add(categoryName.text)
}

// TODO 삭제
fun getInitialSubCategories() =
    mutableStateListOf("반팔 티셔츠", "긴팔 티셔츠", "셔츠", "반팔 셔츠", "니트", "반팔 니트", "니트 베스트")