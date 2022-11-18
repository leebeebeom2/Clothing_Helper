package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PushInitialSubCategoriesUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun pushInitialSubCategories(
        uid: String,
        onUpdateSubCategoriesFail: (FirebaseResult) -> Unit
    ) {
        subCategoryRepository.pushInitialSubCategories(uid)
        onUpdateSubCategoriesFail(subCategoryRepository.loadSubCategories(uid))
    }
}