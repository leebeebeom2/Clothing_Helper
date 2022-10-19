package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.data.model.SubCategoryParent
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository

class WriteInitialSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke() = subCategoryRepository.writeInitialSubCategory()
}

class AddSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(subCategoryParent: SubCategoryParent, name: String) =
        subCategoryRepository.addSubCategory(subCategoryParent, name)
}

class GetAllSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke() = subCategoryRepository.getAllSubCategory()
}