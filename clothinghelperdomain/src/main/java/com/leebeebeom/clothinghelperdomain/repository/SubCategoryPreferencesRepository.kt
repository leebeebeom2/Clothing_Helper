package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SortOrder
import com.leebeebeom.clothinghelperdomain.model.SubCategorySort
import com.leebeebeom.clothinghelperdomain.model.SubCategorySortPreferences
import kotlinx.coroutines.flow.Flow

interface SubCategoryPreferencesRepository {
    val isAllExpanded: Flow<Boolean>
    val sort: Flow<SubCategorySortPreferences>

    suspend fun toggleAllExpand()
    suspend fun changeSort(subCategorySort: SubCategorySort)
    suspend fun changeOrder(sortOrder: SortOrder)
}