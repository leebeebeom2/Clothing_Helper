package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun add(
        name: String,
        parent: MainCategoryType,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val currentTime = System.currentTimeMillis()

        val subCategory = SubCategory(
            name = name,
            parent = parent,
            createDate = currentTime,
            editDate = currentTime
        )

        subCategoryRepository.add(data = subCategory, uid = uid, onFail = onFail)
    }
}