package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.model.toDatabaseModel
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject

class EditSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun nameEdit(
        oldSubCategory: SubCategory,
        name: String,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newSubCategory = oldSubCategory.copy(name = name)

        subCategoryRepository.edit(newData = newSubCategory.toDatabaseModel(), uid = uid, onFail = onFail)
    }
}