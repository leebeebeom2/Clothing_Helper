package com.leebeebeom.clothinghelper.main.subcategory.interfaces

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.AddAndEditContainer
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toUnstable
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase

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