package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class EditSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun nameEdit(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        oldSubCategory: SubCategory,
        name: String,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newSubCategory = oldSubCategory.copy(name = name)

        subCategoryRepository.edit(newData = newSubCategory, uid = uid, onFail = onFail, dispatcher = dispatcher)
    }
}