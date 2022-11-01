package com.leebeebeom.clothinghelper.main.subcategory

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.main.base.BaseSubCategoriesViewModel
import com.leebeebeom.clothinghelper.main.base.BaseSubCategoriesViewModelState
import com.leebeebeom.clothinghelper.util.taskAndReturn
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategoryAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategorySortUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryNameUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val addSubCategoryUseCase: AddSubCategoryUseCase,
    private val editSubCategoryNameUseCase: EditSubCategoryNameUseCase,
    private val subCategoryAllExpandUseCase: SubCategoryAllExpandUseCase,
    private val subCategorySortUseCase: SubCategorySortUseCase
) : BaseSubCategoriesViewModel(
    getUserUseCase,
    getSubCategoryLoadingStateUseCase,
    getSubCategoriesUseCase
) {
    override val viewModelState = SubCategoryViewModelState()

    init {
        collectSubCategories()

        viewModelScope.launch {
            subCategoryAllExpandUseCase.isAllExpand.collect(viewModelState::updateAllExpand)
        }

        viewModelScope.launch {
            subCategorySortUseCase.sortPreferences.collect(viewModelState::updateSort)
        }
    }

    fun addSubCategory(name: String, subCategoryParent: SubCategoryParent) {
        viewModelState.user?.let {
            addSubCategoryUseCase(
                subCategoryParent = subCategoryParent,
                name = name,
                uid = it.uid
            ) { exception ->
                viewModelState.showToast(R.string.add_category_failed)
                Log.d(TAG, "taskFailed: $exception")
            }
        }
    }

    fun toggleAllExpand() = viewModelScope.launch {
        subCategoryAllExpandUseCase.toggleAllExpand()
    }

    fun editSubCategoryName(newName: String) {
        viewModelState.user?.let {
            editSubCategoryNameUseCase(
                viewModelState.selectedSubCategories.first(),
                newName,
                it.uid
            ) { exception ->
                viewModelState.showToast(R.string.add_category_failed)
                Log.d(TAG, "taskFailed: $exception")
            }
        }
    }

    fun changeSort(sort: SubCategorySort) {
        viewModelScope.launch { subCategorySortUseCase.changeSort(sort) }
    }

    fun changeOrder(order: SortOrder) =
        viewModelScope.launch { subCategorySortUseCase.changeOrder(order) }
}

class SubCategoryViewModelState : BaseSubCategoriesViewModelState() {
    var isAllExpand by mutableStateOf(false)
        private set

    fun updateAllExpand(isAllExpand: Boolean) {
        this.isAllExpand = isAllExpand
    }

    var selectedSort by mutableStateOf(SubCategorySort.NAME)
        private set

    var selectedOrder by mutableStateOf(SortOrder.ASCENDING)
        private set

    fun updateSort(subCategorySortPreferences: SubCategorySortPreferences) {
        this.selectedSort = subCategorySortPreferences.sort
        this.selectedOrder = subCategorySortPreferences.sortOrder
    }

    var selectedSubCategories by mutableStateOf(setOf<SubCategory>())
        private set

    val selectedSubCategoriesSize get() = selectedSubCategories.size

    fun onSelect(subCategory: SubCategory) {
        this.selectedSubCategories =
            if (this.selectedSubCategories.contains(subCategory))
                this.selectedSubCategories.taskAndReturn { it.remove(subCategory) }
            else this.selectedSubCategories.taskAndReturn { it.add(subCategory) }
    }

    fun toggleAllSelect(subCategoryParent: SubCategoryParent) {
        val subCategories = getSubCategories(subCategoryParent)

        selectedSubCategories =
            if (selectedSubCategories.size == subCategories.size) emptySet() else subCategories.toSet()
    }

    fun clearSelectedSubCategories() {
        selectedSubCategories = selectedSubCategories.taskAndReturn { it.clear() }
    }
}