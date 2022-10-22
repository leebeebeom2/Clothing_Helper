package com.leebeebeom.clothinghelper.main.maincategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getSubCategoriesUserCase: GetSubCategoriesUserCase
) : ViewModel() {

    val viewModelState = MainCategoryViewModelState()

    init {
        viewModelScope.launch {
            getSubCategoriesUserCase.getTopSubCategories().collect {
                viewModelState.topCategoriesSizeUpdate(it.size)
            }
        }
        viewModelScope.launch {
            getSubCategoriesUserCase.getBottomSubCategories().collect {
                viewModelState.bottomCategoriesSizeUpdate(it.size)
            }
        }
        viewModelScope.launch {
            getSubCategoriesUserCase.getOuterSubCategories().collect {
                viewModelState.outerCategoriesSizeUpdate(it.size)
            }
        }
        viewModelScope.launch {
            getSubCategoriesUserCase.getEtcSubCategories().collect {
                viewModelState.etcCategoriesSizeUpdate(it.size)
            }
        }
    }
}

class MainCategoryViewModelState {
    var topCategoriesSize by mutableStateOf(0)
        private set
    var bottomCategoriesSize by mutableStateOf(0)
        private set
    var outerCategoriesSize by mutableStateOf(0)
        private set
    var etcCategoriesSize by mutableStateOf(0)
        private set

    fun topCategoriesSizeUpdate(size: Int) {
        topCategoriesSize = size
    }

    fun bottomCategoriesSizeUpdate(size: Int) {
        bottomCategoriesSize = size
    }

    fun outerCategoriesSizeUpdate(size: Int) {
        outerCategoriesSize = size
    }

    fun etcCategoriesSizeUpdate(size: Int) {
        etcCategoriesSize = size
    }

    fun getSubCategoriesSize(subCategoryParent: SubCategoryParent): Int {
        return when (subCategoryParent) {
            SubCategoryParent.Top -> topCategoriesSize
            SubCategoryParent.Bottom -> bottomCategoriesSize
            SubCategoryParent.OUTER -> outerCategoriesSize
            SubCategoryParent.ETC -> etcCategoriesSize
        }
    }
}