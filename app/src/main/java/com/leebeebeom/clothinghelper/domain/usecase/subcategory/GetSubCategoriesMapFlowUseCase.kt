package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject

class GetSubCategoriesMapFlowUseCase @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository
) {
    val subCategoriesMapFlow get() = subCategoryRepository.allDataMapFlow
}