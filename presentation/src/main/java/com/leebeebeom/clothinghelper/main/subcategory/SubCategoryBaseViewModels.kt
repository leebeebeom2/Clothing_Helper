package com.leebeebeom.clothinghelper.main.subcategory

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
            uid?.let {
                val result = editSubCategoryNameUseCase.edit(newSubCategory.toUnstable(), it)
                if (result is FirebaseResult.Fail)
                    when (result.exception) {
                        is TimeoutCancellationException -> showToast(R.string.network_error_for_edit_sub_category)
                        else -> showToast(R.string.edit_category_name_failed)
                    }
            }
        }
    }
}

abstract class AddSubCategoryViewModel(
    private val addSubCategoryUseCase: AddSubCategoryUseCase,
) : ViewModel() {
    protected abstract fun showToast(@StringRes text: Int)
    protected abstract val uid: String?

    fun addSubCategory(subCategory: StableSubCategory) {
        viewModelScope.launch {
            uid?.let {
                val result = addSubCategoryUseCase.add(subCategory.toUnstable(), it)
                if (result is FirebaseResult.Fail)
                    when (result.exception) {
                        is TimeoutCancellationException -> showToast(R.string.network_error_for_add_sub_category)
                        else -> showToast(R.string.add_category_failed)
                    }
            }
        }
    }
}