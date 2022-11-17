package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.model.SortOrder
import com.leebeebeom.clothinghelperdomain.model.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SubCategoryAllExpandUseCase @Inject constructor(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    val isAllExpanded get() = subCategoryPreferencesRepository.isAllExpanded

    suspend fun toggleAllExpand() {
        subCategoryPreferencesRepository.toggleAllExpand()
    }
}

@ViewModelScoped
class SubCategorySortUseCase @Inject constructor(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    val sortPreferences get() = subCategoryPreferencesRepository.sort

    suspend fun changeSort(subCategorySort: SubCategorySort) {
        subCategoryPreferencesRepository.changeSort(subCategorySort)
    }

    suspend fun changeOrder(order: SortOrder) {
        subCategoryPreferencesRepository.changeOrder(order)
    }
}