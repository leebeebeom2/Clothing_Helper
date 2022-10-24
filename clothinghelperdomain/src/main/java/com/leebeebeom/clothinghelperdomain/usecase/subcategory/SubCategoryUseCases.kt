package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository

class GetSubCategoriesUserCase(
    private val subCategoryRepository: SubCategoryRepository
) {
    suspend fun loadSubCategories(
        onSubCategoriesLoadingDone: List<() -> Unit>,
        onSubCategoriesLoadingCancelled: List<(Int, String) -> Unit>
    ) = subCategoryRepository.loadSubCategories(
        onSubCategoriesLoadingDone,
        onSubCategoriesLoadingCancelled
    )

    fun getTopSubCategories() = subCategoryRepository.topSubCategories
    fun getBottomSubCategories() = subCategoryRepository.bottomSubCategories
    fun getOuterSubCategories() = subCategoryRepository.outerSubCategories
    fun getEtcSubCategories() = subCategoryRepository.etcSubCategories
}

class AddSubCategoryUseCase(
    private val subCategoryRepository: SubCategoryRepository
) {
    operator fun invoke(
        subCategoryParent: SubCategoryParent,
        name: String,
        addSubCategoryListener: FirebaseListener
    ) = subCategoryRepository.addSubCategory(
        subCategoryParent = subCategoryParent,
        name = name,
        addSubCategoryListener
    )
}

class DeleteSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(subCategory: SubCategory) = subCategoryRepository.deleteSubCategory(subCategory)
}