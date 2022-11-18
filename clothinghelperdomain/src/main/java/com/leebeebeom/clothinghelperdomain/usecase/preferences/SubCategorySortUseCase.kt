package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.repository.preferences.SubCategoryPreferencesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SubCategorySortUseCase @Inject constructor(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    val sortPreferences get() = subCategoryPreferencesRepository.sort

    suspend fun changeSort(sort: Sort) = subCategoryPreferencesRepository.changeSort(sort)

    suspend fun changeOrder(order: Order) = subCategoryPreferencesRepository.changeOrder(order)
}