package com.leebeebeom.clothinghelperdomain.repository

import kotlinx.coroutines.flow.Flow

interface SubCategoryPreferencesRepository {
    val subCategoryPreferences: Flow<SubCategoryPreferences>
    suspend fun toggleAllExpand()
    suspend fun changeSort(subCategorySort: SubCategorySort)
    suspend fun changeOrder(sortOrder: SortOrder)
}

enum class SubCategorySort {
    NAME, CREATE
}

enum class SortOrder {
    ASCENDING, DESCENDING
}

data class SubCategoryPreferences(
    val allExpand: Boolean,
    val sort: SubCategorySort,
    val sortOrder: SortOrder
)