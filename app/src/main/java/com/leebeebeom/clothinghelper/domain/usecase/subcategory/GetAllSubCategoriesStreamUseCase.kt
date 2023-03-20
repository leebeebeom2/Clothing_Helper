package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject

class GetAllSubCategoriesStreamUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    val allSubCategoriesStream get() = subCategoryRepository.allDataStream
}