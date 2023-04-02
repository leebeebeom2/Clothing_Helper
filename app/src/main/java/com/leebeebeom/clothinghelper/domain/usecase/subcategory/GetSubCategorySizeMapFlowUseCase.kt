package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject

class GetSubCategorySizeMapFlowUseCase @Inject constructor(private val subCateRepository: SubCategoryRepository) {
    val subCategorySizeMapFlow get() = subCateRepository.subCategorySizeMapFlow
}