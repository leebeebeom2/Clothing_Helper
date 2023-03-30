package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.ui.drawer.contents.MainCategoryType
import javax.inject.Inject

class AddSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun add(name: String, mainCategoryType: MainCategoryType) =
        subCategoryRepository.add(
            data = SubCategory(
                name = name,
                mainCategoryType = mainCategoryType
            )
        )
}