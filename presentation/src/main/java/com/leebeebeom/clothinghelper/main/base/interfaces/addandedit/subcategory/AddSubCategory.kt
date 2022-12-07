package com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.subcategory

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.BaseContainerAddAndEdit
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toUnstable
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase

interface AddSubCategory : BaseContainerAddAndEdit {
    val addSubCategoryUseCase: AddSubCategoryUseCase

    suspend fun baseAddSubCategory(subCategory: StableSubCategory) {
        uid?.let {
            val result = addSubCategoryUseCase.add(subCategory = subCategory.toUnstable(), uid = it)
            showFailToast(
                result = result,
                networkFailError = R.string.network_error_for_add_sub_category,
                failError = R.string.add_category_failed
            )
        }
    }
}