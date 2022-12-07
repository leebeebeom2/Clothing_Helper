package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.data.SubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun add(name: String, parent: SubCategoryParent, uid: String): FirebaseResult {
        val currentTime = System.currentTimeMillis()
        val subCategory = SubCategory(
            name = name,
            parent = parent,
            createDate = currentTime,
            editDate = currentTime
        )
        return subCategoryRepository.add(t = subCategory, uid = uid)
    }
}