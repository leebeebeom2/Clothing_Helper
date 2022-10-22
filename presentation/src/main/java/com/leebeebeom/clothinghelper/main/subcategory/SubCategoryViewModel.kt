package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val addSubCategoryUseCase: AddSubCategoryUseCase,
    private val getSubCategoriesUserCase: GetSubCategoriesUserCase
) : ViewModel() {
    var viewModelState = SubCategoryViewModelState()
        private set

    init {
        viewModelScope.launch {
            getSubCategoriesUserCase.getTopSubCategories()
                .collect(viewModelState::updateTopSubCategories)
        }
        viewModelScope.launch {
            getSubCategoriesUserCase.getBottomSubCategories()
                .collect(viewModelState::updateBottomSubCategories)
        }
        viewModelScope.launch {
            getSubCategoriesUserCase.getOuterSubCategories()
                .collect(viewModelState::updateOuterSubCategories)
        }
        viewModelScope.launch {
            getSubCategoriesUserCase.getEtcSubCategories()
                .collect(viewModelState::updateEtcSubCategories)
        }
    }

    fun addSubCategory(parent: SubCategoryParent, name: String) = // TODO 로딩 구현
        addSubCategoryUseCase(parent, name, addSubCategoryListener)

    private val addSubCategoryListener = object : FirebaseListener {
        override fun taskSuccess() {}
        override fun taskFailed(exception: Exception?) {} // TODO showToast 구현
    }
}

class SubCategoryViewModelState {
    private var topSubCategories = mutableStateListOf<SubCategory>()
    private var bottomSubCategories = mutableStateListOf<SubCategory>()
    private var outerSubCategories = mutableStateListOf<SubCategory>()
    private var etcSubCategories = mutableStateListOf<SubCategory>()

    fun updateTopSubCategories(topSubCategories: List<SubCategory>) {
        this.topSubCategories.removeAll(this.topSubCategories)
        this.topSubCategories = topSubCategories.toMutableStateList()
    }

    fun updateBottomSubCategories(bottomSubCategories: List<SubCategory>) {
        this.bottomSubCategories.removeAll(this.bottomSubCategories)
        this.bottomSubCategories = bottomSubCategories.toMutableStateList()
    }

    fun updateOuterSubCategories(outerSubCategories: List<SubCategory>) {
        this.outerSubCategories.removeAll(this.outerSubCategories)
        this.outerSubCategories = outerSubCategories.toMutableStateList()
    }

    fun updateEtcSubCategories(etcSubCategories: List<SubCategory>) {
        this.etcSubCategories.removeAll(this.etcSubCategories)
        this.etcSubCategories = etcSubCategories.toMutableStateList()
    }

    fun getSubCategories(parent: SubCategoryParent): SnapshotStateList<SubCategory> {
        return when (parent) {
            SubCategoryParent.TOP -> topSubCategories
            SubCategoryParent.BOTTOM -> bottomSubCategories
            SubCategoryParent.OUTER -> outerSubCategories
            SubCategoryParent.ETC -> etcSubCategories
        }
    }
}