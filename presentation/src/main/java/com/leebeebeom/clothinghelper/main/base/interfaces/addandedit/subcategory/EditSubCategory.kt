package com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.subcategory

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.BaseContainerAddAndEdit
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toUnstable
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryUseCase

interface EditSubCategory : BaseContainerAddAndEdit {
    val editSubCategoryUseCase: EditSubCategoryUseCase

    suspend fun baseEditSubCategory(newSubCategory: StableSubCategory) {
        uid?.let {
            val result = editSubCategoryUseCase.edit(newSubCategory.toUnstable(), it)
            showFailToast(
                result,
                R.string.network_error_for_edit_sub_category,
                R.string.edit_category_name_failed
            )
        }
    }
}