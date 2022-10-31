package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort

class GetSubCategoryAllExpandUseCase(subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    val isAllExpand = subCategoryPreferencesRepository.subCategoryAllExpand
}

class ToggleAllExpandUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend operator fun invoke() = subCategoryPreferencesRepository.toggleAllExpand()
}

class GetSubCategorySortPreferencesUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    operator fun invoke() = subCategoryPreferencesRepository.subCategorySort
}

class ChangeSortUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend operator fun invoke(subCategorySort: SubCategorySort) =
        subCategoryPreferencesRepository.changeSort(subCategorySort)
}

class ChangeOrderUseCate(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend operator fun invoke(order: SortOrder) =
        subCategoryPreferencesRepository.changeOrder(order)
}