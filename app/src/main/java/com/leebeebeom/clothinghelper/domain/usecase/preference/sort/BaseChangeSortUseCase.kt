package com.leebeebeom.clothinghelper.domain.usecase.preference.sort

import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository

abstract class BaseChangeSortUseCase(private val sortPreferenceRepository: SortPreferenceRepository) {
    suspend fun changeSort(sort: Sort) = sortPreferenceRepository.changeSort(sort = sort)
}