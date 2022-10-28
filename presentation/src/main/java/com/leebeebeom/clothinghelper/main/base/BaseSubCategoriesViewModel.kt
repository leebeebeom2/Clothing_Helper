package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.base.BaseViewModelState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadAndGetSubCategoriesUseCase
import kotlinx.coroutines.launch

abstract class BaseSubCategoriesViewModel(
    private val loadAndGetSubCategoriesUseCase: LoadAndGetSubCategoriesUseCase
) : ViewModel() {
    abstract val viewModelState: BaseSubCategoriesViewModelState

    protected fun collectSubCategories() =
        loadAndGetSubCategoriesUseCase.allSubCategories.forEachIndexed { i, subCategoriesFlow ->
            viewModelScope.launch {
                subCategoriesFlow.collect { viewModelState.updateSubCategories(i, it) }
            }
        }
}

open class BaseSubCategoriesViewModelState : BaseViewModelState() {
    private val allSubCategories = List(4) { mutableStateOf(emptyList<SubCategory>()) }

    fun updateSubCategories(index: Int, subCategories: List<SubCategory>) {
        allSubCategories[index].value = subCategories
    }

    val getSubCategories =
        { subCategoryParent: SubCategoryParent -> allSubCategories[subCategoryParent.ordinal].value }

}