package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.base.BaseViewModelState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import kotlinx.coroutines.launch

abstract class BaseSubCategoriesViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase
) : ViewModel() {
    abstract val viewModelState: BaseSubCategoriesViewModelState

    protected fun collectUser() =
        viewModelScope.launch {
            getUserUseCase().collect(viewModelState::updateUser)
        }

    protected fun collectIsLoading() =
        viewModelScope.launch {
            getSubCategoryLoadingStateUseCase.isLoading.collect(viewModelState::updateLoading)
        }

    protected fun collectSubCategories() =
        viewModelScope.launch {
            getSubCategoriesUseCase(scope = this).forEachIndexed { i, subCategoriesFlow ->
                viewModelScope.launch {
                    subCategoriesFlow.collect { viewModelState.updateSubCategories(i, it) }
                }
            }
        }
}

open class BaseSubCategoriesViewModelState : BaseViewModelState() {
    var user: User? by mutableStateOf(null)
        private set

    fun updateUser(user: User?) {
        this.user = user
    }

    var isLoading by mutableStateOf(false)
        private set

    fun updateLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    private val allSubCategories = List(4) { mutableStateOf(emptyList<SubCategory>()) }

    fun updateSubCategories(index: Int, subCategories: List<SubCategory>) {
        allSubCategories[index].value = subCategories
    }

    fun getSubCategories(subCategoryParent: SubCategoryParent) =
        allSubCategories[subCategoryParent.ordinal].value
}