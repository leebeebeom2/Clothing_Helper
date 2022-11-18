package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toUnstable
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryNameUseCase
import kotlinx.coroutines.TimeoutCancellationException

interface AddAndEditContainer {
    fun showToast(@StringRes text: Int)
    val uid: String?

    fun showToastWhenFail(
        result: FirebaseResult,
        @StringRes networkFail: Int,
        @StringRes fail: Int
    ) {
        if (result is FirebaseResult.Fail)
            when (result.exception) {
                is TimeoutCancellationException -> showToast(networkFail)
                else -> showToast(fail)
            }
    }
}

interface AddSubCategory : AddAndEditContainer {
    val addSubCategoryUseCase: AddSubCategoryUseCase

    suspend fun add(subCategory: StableSubCategory) {
        uid?.let {
            val result = addSubCategoryUseCase.add(subCategory.toUnstable(), it)
            showToastWhenFail(
                result,
                R.string.network_error_for_add_sub_category,
                R.string.add_category_failed
            )
        }
    }
}

interface EditSubCategoryName : AddAndEditContainer {
    val editSubCategoryNameUseCase: EditSubCategoryNameUseCase

    suspend fun edit(newSubCategory: StableSubCategory) {
        uid?.let {
            val result = editSubCategoryNameUseCase.edit(newSubCategory.toUnstable(), it)
            showToastWhenFail(
                result,
                R.string.network_error_for_edit_sub_category,
                R.string.edit_category_name_failed
            )
        }
    }
}