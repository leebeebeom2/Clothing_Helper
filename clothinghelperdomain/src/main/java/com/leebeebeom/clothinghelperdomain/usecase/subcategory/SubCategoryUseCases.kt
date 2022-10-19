package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository

class WriteInitialSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke() = subCategoryRepository.writeInitialSubCategory()
}

class AddSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(subCategoryParent: SubCategoryParent, name: String) =
        subCategoryRepository.addSubCategory(subCategoryParent, name)
}

class GetSubCategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    fun getTopSubCategories() = subCategoryRepository.topSubCategories
    fun getBottomSubCategories() = subCategoryRepository.bottomSubCategories
    fun getOuterSubCategories() = subCategoryRepository.outerSubCategories
    fun getETCSubCategories() = subCategoryRepository.etcSubCategories
}