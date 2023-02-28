package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.model.toDatabaseModel
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import javax.inject.Inject

class AddSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun add(
        name: String,
        parent: MainCategoryType,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val subCategory = SubCategory(name = name, parent = parent)

        subCategoryRepository.add(data = subCategory.toDatabaseModel(), uid = uid, onFail = onFail)
    }
}