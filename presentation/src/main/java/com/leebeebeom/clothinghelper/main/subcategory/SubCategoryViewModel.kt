package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    fun addSubCategory(parent: SubCategoryParent, name: String) {
        addSubCategoryUseCase(parent, name, addSubCategoryListener)
    }

    private val addSubCategoryListener = object : FirebaseListener {
        override fun taskSuccess() {}
        override fun taskFailed(exception: Exception?) {} // TODO showToast 구현
    }
}

class SubCategoryViewModelState {
    private var topSubCategories by mutableStateOf(emptyList<SubCategory>())
    private var bottomSubCategories by mutableStateOf(emptyList<SubCategory>())
    private var outerSubCategories by mutableStateOf(emptyList<SubCategory>())
    private var etcSubCategories by mutableStateOf(emptyList<SubCategory>())

    fun updateTopSubCategories(topSubCategories: List<SubCategory>) {
        this.topSubCategories = topSubCategories
    }

    fun updateBottomSubCategories(bottomSubCategories: List<SubCategory>) {
        this.bottomSubCategories = bottomSubCategories
    }

    fun updateOuterSubCategories(outerSubCategories: List<SubCategory>) {
        this.outerSubCategories = outerSubCategories
    }

    fun updateEtcSubCategories(etcSubCategories: List<SubCategory>) {
        this.etcSubCategories = etcSubCategories
    }

    fun getSubCategories(parent: SubCategoryParent): List<SubCategory> {
        return when (parent) {
            SubCategoryParent.TOP -> topSubCategories
            SubCategoryParent.BOTTOM -> bottomSubCategories
            SubCategoryParent.OUTER -> outerSubCategories
            SubCategoryParent.ETC -> etcSubCategories
        }
    }
}