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
import com.leebeebeom.clothinghelperdomain.usecase.preferences.GetSubCategoryAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.GetSubCategorySortPreferencesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.ToggleAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.*
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
    private val getSubCategoryAllExpandUseCase: GetSubCategoryAllExpandUseCase,
    private val toggleAllExpandUseCase: ToggleAllExpandUseCase,
    private val getSubCategorySortPreferencesUseCase: GetSubCategorySortPreferencesUseCase,
    private val changeSortUseCase: ChangeSortUseCase,
    private val changeOrderUserCase: ChangeOrderUserCase
) : BaseSubCategoriesViewModel(
    getUserUseCase,
    getSubCategoryLoadingStateUseCase,
    getSubCategoriesUseCase
) {

    override val viewModelState = SubCategoryViewModelState()

    init {
        collectSubCategories()

        viewModelScope.launch {
            getSubCategoryAllExpandUseCase.isAllExpand.collect(viewModelState::updateAllExpand)
        }

        viewModelScope.launch {
            getSubCategorySortPreferencesUseCase().collect(viewModelState::updateSortPreferences)
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
        toggleAllExpandUseCase()
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

    fun changeSort(sort: SubCategorySort) = {
        viewModelScope.launch {
            changeSortUseCase(sort)
        }
    }

    fun changeOrder(order: SortOrder) {
        viewModelScope.launch {
            changeOrderUserCase(order)
        }
    }
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

    fun updateSortPreferences(subCategorySortPreferences: SubCategorySortPreferences) {
        this.selectedSort = subCategorySortPreferences.sort
        this.selectedOrder = subCategorySortPreferences.sortOrder
    }

    var selectedSubCategories by mutableStateOf(setOf<SubCategory>())
        private set

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