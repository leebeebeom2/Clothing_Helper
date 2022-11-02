package com.leebeebeom.clothinghelper.main.subcategory

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.base.BaseViewModel
import com.leebeebeom.clothinghelper.main.base.BaseSubCategoryUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val addSubCategoryUseCase: AddSubCategoryUseCase,
    private val editSubCategoryNameUseCase: EditSubCategoryNameUseCase,
    private val subCategoryAllExpandUseCase: SubCategoryAllExpandUseCase,
    private val subCategorySortUseCase: SubCategorySortUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SubCategoryUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getUserUseCase(),
                getSubCategoryLoadingStateUseCase(),
                getSubCategoriesUseCase(),
                subCategoryAllExpandUseCase.isAllExpand,
                subCategorySortUseCase.sortPreferences
            ) { user, isLoading, allSubCategories, isAllExpand, sort ->
                SubCategoryUIState(
                    user = user,
                    isLoading = isLoading,
                    allSubCategories = allSubCategories,
                    isAllExpand = isAllExpand,
                    sort = sort
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun addSubCategory(name: String, subCategoryParent: SubCategoryParent) {
        uiState.value.user?.let {
            addSubCategoryUseCase(
                subCategoryParent = subCategoryParent,
                name = name.trim(),
                uid = it.uid
            ) { exception ->
                showToast(R.string.add_category_failed)
                Log.d(TAG, "taskFailed: $exception")
            }
        }
    }

    fun toggleAllExpand() = viewModelScope.launch {
        subCategoryAllExpandUseCase.toggleAllExpand()
    }

    fun editSubCategoryName(newName: String, selectedSubCategory: SubCategory) {
        uiState.value.user?.let {
            editSubCategoryNameUseCase(
                subCategory = selectedSubCategory,
                newName = newName.trim(),
                uid = it.uid
            ) { exception ->
                showToast(R.string.add_category_failed)
                Log.d(TAG, "taskFailed: $exception")
            }
        }
    }

    fun changeSort(sort: SubCategorySort) {
        viewModelScope.launch { subCategorySortUseCase.changeSort(sort) }
    }

    fun changeOrder(order: SortOrder) =
        viewModelScope.launch { subCategorySortUseCase.changeOrder(order) }

    override fun showToast(toastText: Int?) = _uiState.update { it.copy(toastText = toastText) }

    override fun toastShown() = _uiState.update { it.copy(toastText = null) }
}

data class SubCategoryUIState(
    override val toastText: Int? = null,
    override val user: User? = null,
    override val isLoading: Boolean = false,
    override val allSubCategories: List<List<SubCategory>> = List(4) { emptyList() },
    val isAllExpand: Boolean = false,
    val sort: SubCategorySortPreferences = SubCategorySortPreferences(),
) : BaseSubCategoryUIState()