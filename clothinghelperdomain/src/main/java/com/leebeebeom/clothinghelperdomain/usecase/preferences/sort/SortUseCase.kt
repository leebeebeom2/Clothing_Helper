package com.leebeebeom.clothinghelperdomain.usecase.preferences.sort

import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.repository.preferences.SortPreferenceRepository
import kotlinx.coroutines.flow.Flow

interface SortUseCase {
    val sortPreferences: Flow<SortPreferences>

    suspend fun changeSort(sort: Sort)

    suspend fun changeOrder(order: Order)
}

class SortUseCaseImpl(private val sortPreferenceRepository: SortPreferenceRepository) :
    SortUseCase {
    override val sortPreferences = sortPreferenceRepository.sort

    override suspend fun changeSort(sort: Sort) = sortPreferenceRepository.changeSort(sort)

    override suspend fun changeOrder(order: Order) = sortPreferenceRepository.changeOrder(order)
}