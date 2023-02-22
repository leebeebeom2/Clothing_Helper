package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

// TODO edit -> nameEdit 으로 변경

@ViewModelScoped
class EditSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun edit(
        oldSubCategory: SubCategory,
        name: String,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newSubCategory = oldSubCategory.copy(name = name, editDate = System.currentTimeMillis(), isSynced = false)

        subCategoryRepository.edit(newData = newSubCategory, uid = uid, onFail = onFail)
    }
}