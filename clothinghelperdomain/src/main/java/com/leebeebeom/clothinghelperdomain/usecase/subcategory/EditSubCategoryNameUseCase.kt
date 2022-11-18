package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class EditSubCategoryNameUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun edit(newSubCategory: SubCategory, uid: String) =
        subCategoryRepository.editSubCategoryName(newSubCategory, uid)
}