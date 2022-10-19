package com.leebeebeom.clothinghelper.ui.main.subcategory

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class SubCategoryViewModel : ViewModel() {
    var viewModelState = SubCategoryViewModelState()
        private set
}

data class SubCategoryViewModelState(
    val subCategories: SnapshotStateList<String> = getInitialSubCategories(),
) {
    fun addNewCategory(categoryName: String) = subCategories.add(categoryName)
}

// TODO 삭제
fun getInitialSubCategories() =
    mutableStateListOf("반팔 티셔츠", "긴팔 티셔츠", "셔츠", "반팔 셔츠", "니트", "반팔 니트", "니트 베스트")