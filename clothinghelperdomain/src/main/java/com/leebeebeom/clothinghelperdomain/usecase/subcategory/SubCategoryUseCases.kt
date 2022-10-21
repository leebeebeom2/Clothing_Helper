package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository

class GetSubCategoriesUserCase(
    private val subCategoryRepository: SubCategoryRepository
) {
    suspend fun loadSubCategories(
        onDone: List<() -> Unit>,
        onCancelled: List<(Int, String) -> Unit>
    ) = subCategoryRepository.loadSubCategories(onDone, onCancelled)

    fun getTopSubCategories() = subCategoryRepository.topSubCategories
    fun getBottomSubCategories() = subCategoryRepository.bottomSubCategories
    fun getOuterSubCategories() = subCategoryRepository.outerSubCategories
    fun getEtcSubCategories() = subCategoryRepository.etcSubCategories
}

class AddSubCategoryUseCase(
    private val subCategoryRepository: SubCategoryRepository
) {
    operator fun invoke(
        uid: String,
        subCategoryParent: SubCategoryParent,
        name: String,
        addSubCategoryListener: FirebaseListener
    ) =
        subCategoryRepository.addSubCategory(
            uid = uid,
            subCategoryParent = subCategoryParent,
            name = name,
            addSubCategoryListener
        )
}