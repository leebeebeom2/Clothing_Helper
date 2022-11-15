package com.leebeebeom.clothinghelperdata.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SubCategoryPreferencesRepositoryImpl(private val subCategoryDataStore: DataStore<Preferences>) :
    SubCategoryPreferencesRepository {
    override val isAllExpand: Flow<Boolean> = subCategoryDataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }.map { it[SubCategoryPreferenceKeys.ALL_EXPAND] ?: false }

    override val sort: Flow<SubCategorySortPreferences> = subCategoryDataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map {
            val sort = it[SubCategoryPreferenceKeys.SORT] ?: SubCategorySort.NAME.name
            val order = it[SubCategoryPreferenceKeys.ORDER] ?: SortOrder.ASCENDING.name
            SubCategorySortPreferences(enumValueOf(sort), enumValueOf(order))
        }

    override suspend fun toggleAllExpand() {
        subCategoryDataStore.edit {
            val current = it[SubCategoryPreferenceKeys.ALL_EXPAND] ?: false
            it[SubCategoryPreferenceKeys.ALL_EXPAND] = !current
        }
    }

    override suspend fun changeSort(subCategorySort: SubCategorySort) {
        subCategoryDataStore.edit {
            it[SubCategoryPreferenceKeys.SORT] = subCategorySort.name
        }
    }

    override suspend fun changeOrder(sortOrder: SortOrder) {
        subCategoryDataStore.edit {
            it[SubCategoryPreferenceKeys.ORDER] = sortOrder.name
        }
    }
}

private object SubCategoryPreferenceKeys {
    val ALL_EXPAND = booleanPreferencesKey("all_expand")
    val SORT = stringPreferencesKey("sort")
    val ORDER = stringPreferencesKey("order")
}