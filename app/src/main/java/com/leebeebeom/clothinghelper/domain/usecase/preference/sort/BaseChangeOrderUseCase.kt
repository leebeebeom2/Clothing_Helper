package com.leebeebeom.clothinghelper.domain.usecase.preference.sort

import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository

abstract class BaseChangeOrderUseCase(private val sortPreferenceRepository: SortPreferenceRepository) {
    suspend fun changeOrder(order: Order) = sortPreferenceRepository.changeOrder(order)
}