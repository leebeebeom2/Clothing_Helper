package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository

class WriteInitialSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke() = subCategoryRepository.writeInitialSubCategory()
}