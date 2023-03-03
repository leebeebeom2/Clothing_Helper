package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun add(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        name: String,
        mainCategoryType: MainCategoryType,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = withContext(dispatcher) {
        val subCategory = SubCategory(name = name, mainCategoryType = mainCategoryType)

        subCategoryRepository.add(data = subCategory, uid = uid, onFail = onFail, dispatcher = dispatcher)
    }
}