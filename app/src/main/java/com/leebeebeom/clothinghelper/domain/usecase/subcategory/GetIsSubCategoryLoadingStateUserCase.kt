package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject

class GetIsSubCategoryLoadingStateUserCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    val isLoading get() = subCategoryRepository.isLoading
}