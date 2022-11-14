package com.leebeebeom.clothinghelper.main.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toUnstable
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryNameUseCase
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch

abstract class EditSubCategoryNameViewModel(
    private val editSubCategoryNameUseCase: EditSubCategoryNameUseCase,
    addSubCategoryUseCase: AddSubCategoryUseCase
) : AddSubCategoryViewModel(addSubCategoryUseCase) {

    open fun editSubCategoryName(newSubCategory: StableSubCategory) {
        viewModelScope.launch {
            val result = editSubCategoryNameUseCase(newSubCategory = newSubCategory.toUnstable())
            if (result is FirebaseResult.Fail)
                when (result.exception) {
                    is TimeoutCancellationException -> showToast(R.string.network_error_for_edit_sub_category)
                    else -> showToast(R.string.edit_category_name_failed)
                }
        }
    }
}

abstract class AddSubCategoryViewModel(
    private val addSubCategoryUseCase: AddSubCategoryUseCase,
) : ViewModel() {
    protected abstract fun showToast(@StringRes text: Int)

    fun addSubCategory(subCategory: StableSubCategory) {
        viewModelScope.launch {
            val result = addSubCategoryUseCase(subCategory = subCategory.toUnstable())
            if (result is FirebaseResult.Fail)
                when (result.exception) {
                    is TimeoutCancellationException -> showToast(R.string.network_error_for_add_sub_category)
                    else -> showToast(R.string.add_category_failed)
                }
        }
    }
}