package com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.subcategory

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.BaseContainerAddAndEdit
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase

interface AddSubCategory : BaseContainerAddAndEdit {
    val addSubCategoryUseCase: AddSubCategoryUseCase

    suspend fun baseAddSubCategory(name: String, parent: SubCategoryParent) {
        uid?.let {
            val result = addSubCategoryUseCase.add(name = name, parent = parent, uid = it)
            showFailToast(
                result = result,
                networkFailError = R.string.network_error_for_add_sub_category,
                failError = R.string.add_category_failed
            )
        }
    }
}