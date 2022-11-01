package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort

class SubCategoryAllExpandUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    val isAllExpand get() = subCategoryPreferencesRepository.allExpand
}

class ToggleAllExpandUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend operator fun invoke() = subCategoryPreferencesRepository.toggleAllExpand()
}

class SubCategorySortUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend fun changeSort(subCategorySort: SubCategorySort) =
        subCategoryPreferencesRepository.changeSort(subCategorySort)

    suspend fun changeOrder(order: SortOrder) = subCategoryPreferencesRepository.changeOrder(order)
}