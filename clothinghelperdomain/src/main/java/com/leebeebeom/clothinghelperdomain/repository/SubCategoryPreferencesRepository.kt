package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import kotlinx.coroutines.flow.Flow

interface SubCategoryPreferencesRepository {
    val sort: Flow<SortPreferences>

    suspend fun changeSort(sort: Sort)
    suspend fun changeOrder(order: Order)
}