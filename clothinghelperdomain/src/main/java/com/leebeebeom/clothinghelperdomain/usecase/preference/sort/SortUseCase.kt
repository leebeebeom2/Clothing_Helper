package com.leebeebeom.clothinghelperdomain.usecase.preference.sort

import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.flow.Flow

interface SortUseCase {
    val sortPreferences: Flow<SortPreferences>

    suspend fun changeSort(sort: Sort)

    suspend fun changeOrder(order: Order)
}

class SortUseCaseImpl(private val sortPreferenceRepository: SortPreferenceRepository) :
    SortUseCase {
    override val sortPreferences get() = sortPreferenceRepository.sort

    override suspend fun changeSort(sort: Sort) = sortPreferenceRepository.changeSort(sort = sort)

    override suspend fun changeOrder(order: Order) =
        sortPreferenceRepository.changeOrder(order = order)
}