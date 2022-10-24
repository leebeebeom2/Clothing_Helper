package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository

class LoadSubCategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(
        onSubCategoriesLoadingDone: List<() -> Unit>,
        onSubCategoriesLoadingCancelled: List<(Int, String) -> Unit>
    ) = subCategoryRepository.loadSubCategories(
        onSubCategoriesLoadingDone,
        onSubCategoriesLoadingCancelled
    )

}

class GetSubCategoriesUseCase(subCategoryRepository: SubCategoryRepository) {
    val topSubCategories = subCategoryRepository.topSubCategories
    val bottomSubCategories = subCategoryRepository.bottomSubCategories
    val outerSubCategories = subCategoryRepository.outerSubCategories
    val etcSubCategories = subCategoryRepository.etcSubCategories
}

class LoadAndGetSubCategoriesUseCase(
    private val loadSubCategoriesUseCase: LoadSubCategoriesUseCase,
    getSubCategoriesUseCase: GetSubCategoriesUseCase
) {
    suspend fun loadSubCategories(
        onSubCategoriesLoadingDone: List<() -> Unit>,
        onSubCategoriesLoadingCancelled: List<(Int, String) -> Unit>
    ) = loadSubCategoriesUseCase(
        onSubCategoriesLoadingDone,
        onSubCategoriesLoadingCancelled
    )

    val topSubCategories = getSubCategoriesUseCase.topSubCategories
    val bottomSubCategories = getSubCategoriesUseCase.bottomSubCategories
    val outerSubCategories = getSubCategoriesUseCase.outerSubCategories
    val etcSubCategories = getSubCategoriesUseCase.etcSubCategories
}

class AddSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
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
    operator fun invoke(subCategory: SubCategory, deleteSubCategoryListener: FirebaseListener) =
        subCategoryRepository.deleteSubCategory(subCategory, deleteSubCategoryListener)
}