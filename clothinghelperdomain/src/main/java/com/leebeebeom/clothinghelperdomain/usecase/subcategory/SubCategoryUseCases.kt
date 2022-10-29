package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository

class LoadSubCategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(
        onSubCategoriesLoadingDone: () -> Unit,
        onSubCategoriesLoadingCancelled: (Int, String) -> Unit
    ) = subCategoryRepository.loadSubCategories(
        onSubCategoriesLoadingDone,
        onSubCategoriesLoadingCancelled
    )
}

class GetSubCategoriesUseCase(subCategoryRepository: SubCategoryRepository) {
    val allSubCategories = subCategoryRepository.allSubCategories
}

class LoadAndGetSubCategoriesUseCase(
    private val loadSubCategoriesUseCase: LoadSubCategoriesUseCase,
    getSubCategoriesUseCase: GetSubCategoriesUseCase
) {
    suspend fun loadSubCategories(
        onSubCategoriesLoadingDone: () -> Unit,
        onSubCategoriesLoadingCancelled: (errorCode: Int, message: String) -> Unit
    ) = loadSubCategoriesUseCase(
        onSubCategoriesLoadingDone,
        onSubCategoriesLoadingCancelled
    )

    val allSubCategories = getSubCategoriesUseCase.allSubCategories
}

class AddSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(
        subCategoryParent: SubCategoryParent,
        name: String,
        taskFailed: (Exception?) -> Unit
    ) = subCategoryRepository.addSubCategory(
        subCategoryParent = subCategoryParent,
        name = name,
        taskFailed
    )
}

class EditSubCategoryNameUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(
        subCategory: SubCategory,
        newName: String
    ) = subCategoryRepository.editSubCategoryName(subCategory, newName)
}

class SortSubCategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke() = subCategoryRepository.sortSubCategories()
}