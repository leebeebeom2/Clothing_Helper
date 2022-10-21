package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val addSubCategoryUseCase: AddSubCategoryUseCase
) : ViewModel() {
    var viewModelState = SubCategoryViewModelState()
        private set

    fun addSubCategory(name: String) {
        val uid = getUserUseCase().value?.uid!! // TODO 수정
        addSubCategoryUseCase(
            uid,
            SubCategoryParent.Top,
            name,
            addSubCategoryListener
        ) // TODO 패런트 로직 구현
    }

    val addSubCategoryListener = object : FirebaseListener {
        override fun taskSuccess() {} // TODO 구현
        override fun taskFailed(exception: Exception?) {} // TODO 구현
    }
}

data class SubCategoryViewModelState(
    val subCategories: SnapshotStateList<String> = getInitialSubCategories(),
)

// TODO 삭제
fun getInitialSubCategories() =
    mutableStateListOf("반팔 티셔츠", "긴팔 티셔츠", "셔츠", "반팔 셔츠", "니트", "반팔 니트", "니트 베스트")