package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class EditSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun edit(
        oldSubCategory: SubCategory,
        name: String,
        uid: String,
    ): FirebaseResult {
        val newSubCategory = oldSubCategory.copy(name = name, editDate = System.currentTimeMillis())

        return subCategoryRepository.edit(newData = newSubCategory, uid = uid)
    }
}