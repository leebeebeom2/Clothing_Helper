package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject

class GetSubCategoryNamesMapFlowUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    val subCategoryNamesMapFlow get() = subCategoryRepository.dataNamesMapFlow
}