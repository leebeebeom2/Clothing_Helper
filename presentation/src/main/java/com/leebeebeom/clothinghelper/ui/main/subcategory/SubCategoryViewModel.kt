package com.leebeebeom.clothinghelper.ui.main.subcategory

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.data.model.SubCategoryParent
import com.leebeebeom.clothinghelper.domain.usecase.subcategory.AddSubCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val addSubCategoryUseCase: AddSubCategoryUseCase
) : ViewModel() {
    var viewModelState = SubCategoryViewModelState()
        private set

    fun addSubCategory(name: String) {
        addSubCategoryUseCase(SubCategoryParent.Top, name) // TODO 패런트 로직 구현
    }
}

data class SubCategoryViewModelState(
    val subCategories: SnapshotStateList<String> = getInitialSubCategories(),
)

// TODO 삭제
fun getInitialSubCategories() =
    mutableStateListOf("반팔 티셔츠", "긴팔 티셔츠", "셔츠", "반팔 셔츠", "니트", "반팔 니트", "니트 베스트")