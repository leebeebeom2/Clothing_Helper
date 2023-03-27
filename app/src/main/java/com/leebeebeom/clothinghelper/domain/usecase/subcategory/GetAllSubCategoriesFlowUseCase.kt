package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject

class GetAllSubCategoriesFlowUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    val allSubCategoriesFlow get() = subCategoryRepository.allDataFlow
}