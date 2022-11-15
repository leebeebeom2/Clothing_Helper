package com.leebeebeom.clothinghelperdomain.repository

import kotlinx.coroutines.flow.Flow

interface SubCategoryPreferencesRepository {
    val isAllExpand: Flow<Boolean>
    val sort: Flow<SubCategorySortPreferences>

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

data class SubCategorySortPreferences(
    val sort: SubCategorySort = SubCategorySort.NAME,
    val sortOrder: SortOrder = SortOrder.ASCENDING
)