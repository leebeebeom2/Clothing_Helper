package com.leebeebeom.clothinghelper.main.subcategory.interfaces

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.AddAndEditContainer
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toUnstable
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryNameUseCase

interface EditSubCategoryName : AddAndEditContainer {
    val editSubCategoryNameUseCase: EditSubCategoryNameUseCase

    suspend fun baseEditSubCategoryName(newSubCategory: StableSubCategory) {
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