package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository

class GetSubCategoriesUserCase(
    private val getTopSubcategoriesUseCase: GetTopSubcategoriesUseCase,
    private val getBottomSubcategoriesUseCase: GetBottomSubcategoriesUseCase,
    private val getOuterSubcategoriesUseCase: GetOuterSubcategoriesUseCase,
    private val getEtcSubcategoriesUseCase: GetEtcSubcategoriesUseCase
) {
    fun getTopSubCategories(uid: String, onCancelled: (Int, String) -> Unit) =
        getTopSubcategoriesUseCase.invoke(uid, onCancelled)

    fun getBottomSubCategories(uid: String, onCancelled: (Int, String) -> Unit) =
        getBottomSubcategoriesUseCase.invoke(uid, onCancelled)

    fun getOuterSubCategories(uid: String, onCancelled: (Int, String) -> Unit) =
        getOuterSubcategoriesUseCase.invoke(uid, onCancelled)


    fun getEtcSubCategories(uid: String, onCancelled: (Int, String) -> Unit) =
        getEtcSubcategoriesUseCase.invoke(uid, onCancelled)
}

class GetTopSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(uid: String, onCancelled: (Int, String) -> Unit) =
        subCategoryRepository.getTopSubCategories(uid, onCancelled)
}

class GetBottomSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(uid: String, onCancelled: (Int, String) -> Unit) =
        subCategoryRepository.getBottomSubCategories(uid, onCancelled)
}

class GetOuterSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(uid: String, onCancelled: (Int, String) -> Unit) =
        subCategoryRepository.getOuterSubCategories(uid, onCancelled)
}

class GetEtcSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(uid: String, onCancelled: (Int, String) -> Unit) =
        subCategoryRepository.getEtcSubCategories(uid, onCancelled)
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