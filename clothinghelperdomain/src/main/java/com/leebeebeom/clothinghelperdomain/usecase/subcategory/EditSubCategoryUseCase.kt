package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.data.SubCategory
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class EditSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun edit(oldSubCategory: SubCategory, name: String, uid: String): FirebaseResult {
        val newSubCategory = oldSubCategory.copy(name = name, editDate = System.currentTimeMillis())

        return subCategoryRepository.edit(newT = newSubCategory, uid = uid)
    }
}