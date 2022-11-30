package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.data.SubCategory
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun add(subCategory: SubCategory, uid: String) =
        subCategoryRepository.add(subCategory, uid)
}