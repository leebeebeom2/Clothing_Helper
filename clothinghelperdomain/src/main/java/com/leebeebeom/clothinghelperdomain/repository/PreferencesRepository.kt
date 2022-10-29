package com.leebeebeom.clothinghelperdomain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val subCategoryPreferences: Flow<SubCategoryPreferences>
    suspend fun toggleAllExpand()
}

enum class SubCategorySort {
    NAME, CREATE
}

enum class SortOrder {
    ASCENDING, DESCENDING
}

data class SubCategoryPreferences(
    val allExpand: Boolean
)