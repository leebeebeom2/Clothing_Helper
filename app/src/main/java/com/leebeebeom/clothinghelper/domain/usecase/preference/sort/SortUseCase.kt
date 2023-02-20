package com.leebeebeom.clothinghelper.domain.usecase.preference.sort

import com.leebeebeom.clothinghelper.domain.model.Order
import com.leebeebeom.clothinghelper.domain.model.Sort
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository

abstract class SortUseCase(private val sortPreferenceRepository: SortPreferenceRepository) {
    val sortPreferences get() = sortPreferenceRepository.sort

    suspend fun changeSort(sort: Sort) = sortPreferenceRepository.changeSort(sort = sort)

    suspend fun changeOrder(order: Order) = sortPreferenceRepository.changeOrder(order = order)
}