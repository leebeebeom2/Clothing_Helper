package com.leebeebeom.clothinghelper.main.maincategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase
) :
    ViewModel() {

    val viewModelState = MainCategoryViewModelState()

    init {
        viewModelScope.launch {
            getSubCategoryLoadingStateUseCase.isLoading.collect(viewModelState::updateIsLoading)
        }

        viewModelScope.launch {
            getSubCategoriesUseCase(scope = this).forEachIndexed { i, subCategoriesFlow ->
                viewModelScope.launch {
                    subCategoriesFlow.collect { viewModelState.categoriesSizeUpdate(i, it.size) }
                }
            }
        }
    }
}

class MainCategoryViewModelState {
    var isLoading by mutableStateOf(false)
        private set

    fun updateIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    private val allSubCategoriesSize = List(4) { mutableStateOf(0) }

    fun categoriesSizeUpdate(index: Int, size: Int) {
        allSubCategoriesSize[index].value = size
    }

    fun getSubCategoriesSize(subCategoryParent: SubCategoryParent): Int =
        allSubCategoriesSize[subCategoryParent.ordinal].value
}