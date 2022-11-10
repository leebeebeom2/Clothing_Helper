package com.leebeebeom.clothinghelper.main.subcategory

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.main.base.BaseIsAllExpandState
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
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
    private val getUserUseCase: GetUserUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val addSubCategoryUseCase: AddSubCategoryUseCase,
    private val editSubCategoryNameUseCase: EditSubCategoryNameUseCase,
    private val subCategoryAllExpandUseCase: SubCategoryAllExpandUseCase,
    private val subCategorySortUseCase: SubCategorySortUseCase
) : ViewModel() {

    val uiStates = SubCategoryUIState()

    init {
        viewModelScope.launch {
            getUserUseCase().collect(uiStates::updateUser)
        }

        viewModelScope.launch {
            getSubCategoryLoadingStateUseCase().collect(uiStates::updateIsLoading)
        }

        viewModelScope.launch {
            getSubCategoriesUseCase().collect(uiStates::updateAllSubCategories)
        }

        viewModelScope.launch {
            subCategoryAllExpandUseCase.isAllExpand.collect(uiStates::updateIsAllExpand)
        }

        viewModelScope.launch {
            subCategorySortUseCase.sortPreferences.collect(uiStates::updateSort)
        }
    }

    fun addSubCategory(name: String, subCategoryParent: SubCategoryParent) =
        viewModelScope.launch {
            uiStates.user?.let {
                val result = addSubCategoryUseCase(
                    subCategoryParent = subCategoryParent, name = name.trim(), uid = it.uid
                )

                if (result is FirebaseResult.Fail) {
                    uiStates.showToast(R.string.add_category_failed)
                    Log.d(TAG, "taskFailed: $result")
                }
            } ?: uiStates.showToast(R.string.add_category_failed)
        }

    fun toggleAllExpand() = viewModelScope.launch {
        subCategoryAllExpandUseCase.toggleAllExpand()
    }

    fun editSubCategoryName(parent: SubCategoryParent, key: String?, newName: String) =
        viewModelScope.launch {
            uiStates.user?.let { user ->
                key?.let { key ->
                    val result = editSubCategoryNameUseCase(
                        parent = parent,
                        key = key,
                        newName = newName.trim(),
                        uid = user.uid
                    )

                    if (result is FirebaseResult.Fail) {
                        uiStates.showToast(R.string.add_category_failed)
                        Log.d(TAG, "taskFailed: $result")
                    }
                } ?: uiStates.showToast(R.string.add_category_failed)
            } ?: uiStates.showToast(R.string.add_category_failed)
        }

    fun changeSort(sort: SubCategorySort) {
        viewModelScope.launch { subCategorySortUseCase.changeSort(sort) }
    }

    fun changeOrder(order: SortOrder) =
        viewModelScope.launch { subCategorySortUseCase.changeOrder(order) }
}

class SubCategoryUIState : BaseIsAllExpandState() {
    var sort by mutableStateOf(SubCategorySortPreferences())
        private set

    fun updateSort(sort: SubCategorySortPreferences) {
        this.sort = sort
    }
}