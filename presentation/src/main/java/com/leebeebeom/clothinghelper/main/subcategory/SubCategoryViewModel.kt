package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.base.ToastUIState
import com.leebeebeom.clothinghelper.base.ToastUIStateImpl
import com.leebeebeom.clothinghelper.main.base.interfaces.*
import com.leebeebeom.clothinghelper.main.subcategory.interfaces.AddSubCategory
import com.leebeebeom.clothinghelper.main.subcategory.interfaces.EditSubCategoryName
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.GetDataLoadingStateUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.sort.SubCategorySortUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryNameUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetAllSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val getDataLoadingStateUseCase: GetDataLoadingStateUseCase,
    private val subCategorySortUseCase: SubCategorySortUseCase,
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase,
    private val getUserUseCase: GetUserUseCase,
    override val addSubCategoryUseCase: AddSubCategoryUseCase,
    override val editSubCategoryNameUseCase: EditSubCategoryNameUseCase,
) : AddSubCategory, EditSubCategoryName, ViewModel() {

    private val uiState = SubCategoryScreenUIState()

    fun getUiState(parent: SubCategoryParent): SubCategoryScreenUIState {
        uiState.setSubCategories(parent)
        return uiState
    }

    init {
        viewModelScope.launch {
            launch { getDataLoadingStateUseCase.isLoading.collectLatest(uiState::updateIsLoading) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
            launch { subCategorySortUseCase.sortPreferences.collectLatest(uiState::updateSort) }
            launch { getUserUseCase.user.collectLatest(uiState::updateUser) }
        }
    }

    override fun showToast(text: Int) = uiState.showToast(text)
    override val uid get() = uiState.user?.uid
    fun changeSort(sort: Sort) = viewModelScope.launch { subCategorySortUseCase.changeSort(sort) }
    fun changeOrder(order: Order) =
        viewModelScope.launch { subCategorySortUseCase.changeOrder(order) }

    fun addSubCategory(subCategory: StableSubCategory) {
        viewModelScope.launch {
            super.baseAddSubCategory(subCategory = subCategory)
        }
    }

    fun editSubCategoryName(newSubCategory: StableSubCategory) {
        viewModelScope.launch {
            uiState.selectModeOff()
            super.baseEditSubCategoryName(newSubCategory)
        }
    }
}

class SubCategoryScreenUIState :
    ToastUIState by ToastUIStateImpl(),
    LoadingUIState by LoadingUIStateImpl(),
    UserUIState by UserUIStateImpl(),
    SubCategoryUIState by SubCategoryUIStateImpl(),
    SortUIState by SortUIStateImpl(),
    SelectMode<StableSubCategory> by SelectModeImpl() {

    override var list by mutableStateOf(emptyList<StableSubCategory>().toImmutableList())
        private set

    fun setSubCategories(parent: SubCategoryParent) {
        list = getSubCategories(parent)
    }
}