package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllSubCategoriesUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    val allSubCategories get() = subCategoryRepository.allData
}