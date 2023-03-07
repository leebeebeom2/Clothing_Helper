package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject

class EditSubCategoryNameUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun nameEdit(
        oldSubCategory: SubCategory,
        name: String,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newSubCategory = oldSubCategory.copy(name = name)

        subCategoryRepository.edit(
            oldData = oldSubCategory,
            newData = newSubCategory,
            uid = uid,
            onFail = onFail,
        )
    }
}