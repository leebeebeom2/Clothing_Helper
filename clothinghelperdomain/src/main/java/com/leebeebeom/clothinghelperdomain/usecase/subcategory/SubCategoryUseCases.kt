package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository

class WriteInitialSubCategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke() = subCategoryRepository.writeInitialSubCategory()
}

class GetSubCategoriesUserCase(
    private val getTopSubcategoriesUseCase: GetTopSubcategoriesUseCase,
    private val getBottomSubcategoriesUseCase: GetBottomSubcategoriesUseCase,
    private val getOuterSubcategoriesUseCase: GetOuterSubcategoriesUseCase,
    private val getEtcSubcategoriesUseCase: GetEtcSubcategoriesUseCase
) {
    fun getTopSubCategories(onCancelled: (Int, String) -> Unit) =
        getTopSubcategoriesUseCase.invoke(onCancelled)

    fun getBottomSubCategories(onCancelled: (Int, String) -> Unit) =
        getBottomSubcategoriesUseCase.invoke(onCancelled)

    fun getOuterSubCategories(onCancelled: (Int, String) -> Unit) =
        getOuterSubcategoriesUseCase.invoke(onCancelled)

    fun getEtcSubCategories(onCancelled: (Int, String) -> Unit) =
        getEtcSubcategoriesUseCase.invoke(onCancelled)
}

class GetTopSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(onCancelled: (Int, String) -> Unit) =
        subCategoryRepository.getTopSubCategories(onCancelled)
}

class GetBottomSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(onCancelled: (Int, String) -> Unit) =
        subCategoryRepository.getBottomSubCategories(onCancelled)
}

class GetOuterSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(onCancelled: (Int, String) -> Unit) =
        subCategoryRepository.getOuterSubCategories(onCancelled)
}

class GetEtcSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(onCancelled: (Int, String) -> Unit) =
        subCategoryRepository.getEtcSubCategories(onCancelled)
}


class AddSubCategoryUseCase(
    private val subCategoryRepository: SubCategoryRepository
) {
    suspend operator fun invoke(
        subCategoryParent: SubCategoryParent,
        name: String,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) = subCategoryRepository.addSubCategory(
        subCategoryParent = subCategoryParent,
        name = name,
        onSuccess = onSuccess,
        onFailed = onFailed
    )
}