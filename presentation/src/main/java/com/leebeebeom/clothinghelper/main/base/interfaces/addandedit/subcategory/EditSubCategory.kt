package com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.subcategory

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.BaseContainerAddAndEdit
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toUnstable
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryUseCase

interface EditSubCategory : BaseContainerAddAndEdit {
    val editSubCategoryUseCase: EditSubCategoryUseCase

    suspend fun baseEditSubCategory(oldSubCategory: StableSubCategory, name: String) {
        uid?.let {
            val result = editSubCategoryUseCase.edit(
                oldSubCategory = oldSubCategory.toUnstable(),
                name = name,
                uid = it
            )
            showFailToast(
                result = result,
                networkFailError = R.string.network_error_for_edit_sub_category,
                failError = R.string.edit_category_name_failed
            )
        }
    }
}