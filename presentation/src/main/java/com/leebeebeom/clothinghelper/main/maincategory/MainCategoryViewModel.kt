package com.leebeebeom.clothinghelper.main.maincategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadAndGetSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val loadAndGetSubCategoriesUseCase: LoadAndGetSubCategoriesUseCase
) : ViewModel() {

    val viewModelState = MainCategoryViewModelState()

    init {
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.topSubCategories.collect {
                viewModelState.topCategoriesSizeUpdate(it.size)
            }
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.bottomSubCategories.collect {
                viewModelState.bottomCategoriesSizeUpdate(it.size)
            }
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.outerSubCategories.collect {
                viewModelState.outerCategoriesSizeUpdate(it.size)
            }
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.etcSubCategories.collect {
                viewModelState.etcCategoriesSizeUpdate(it.size)
            }
        }
    }
}

class MainCategoryViewModelState {
    private var topCategoriesSize by mutableStateOf(0)
    private var bottomCategoriesSize by mutableStateOf(0)
    private var outerCategoriesSize by mutableStateOf(0)
    private var etcCategoriesSize by mutableStateOf(0)

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
            SubCategoryParent.TOP -> topCategoriesSize
            SubCategoryParent.BOTTOM -> bottomCategoriesSize
            SubCategoryParent.OUTER -> outerCategoriesSize
            SubCategoryParent.ETC -> etcCategoriesSize
        }
    }
}