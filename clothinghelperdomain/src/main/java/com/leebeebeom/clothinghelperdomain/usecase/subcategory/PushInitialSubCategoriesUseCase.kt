package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.data.SubCategory
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PushInitialSubCategoriesUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun pushInitialSubCategories(
        uid: String,
        onLoadSubCategoriesFail: (FirebaseResult.Fail) -> Unit
    ) {
        subCategoryRepository.pushInitialSubCategories(uid = uid)
        val result = subCategoryRepository.load(uid = uid, type = SubCategory::class.java)
        if (result is FirebaseResult.Fail) onLoadSubCategoriesFail(result)
    }
}