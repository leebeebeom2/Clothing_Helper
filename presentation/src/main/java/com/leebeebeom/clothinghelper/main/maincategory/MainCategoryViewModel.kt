package com.leebeebeom.clothinghelper.main.maincategory

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadAndGetSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    loadAndGetSubCategoriesUseCase: LoadAndGetSubCategoriesUseCase
) : ViewModel() {

    val viewModelState = MainCategoryViewModelState()

    init {
        loadAndGetSubCategoriesUseCase.allSubCategories.forEachIndexed { i, subCategoriesFlow ->
            viewModelScope.launch {
                subCategoriesFlow.collect {
                    viewModelState.categoriesSizeUpdate(i, it.size)
                }
            }
        }
    }
}

class MainCategoryViewModelState {
    private val allSubCategoriesSize = List(4) { mutableStateOf(0) }

    fun categoriesSizeUpdate(index: Int, size: Int) {
        allSubCategoriesSize[index].value = size
    }

    fun getSubCategoriesSize(subCategoryParent: SubCategoryParent): Int =
        allSubCategoriesSize[subCategoryParent.ordinal].value
}