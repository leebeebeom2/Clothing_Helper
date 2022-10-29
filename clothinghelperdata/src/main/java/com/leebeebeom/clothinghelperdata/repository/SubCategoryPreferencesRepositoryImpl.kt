package com.leebeebeom.clothinghelperdata.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferences
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SubCategoryPreferencesRepositoryImpl(private val subCategoryDataStore: DataStore<Preferences>) :
    SubCategoryPreferencesRepository {

    override val subCategoryPreferences: Flow<SubCategoryPreferences> = subCategoryDataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map {
            val allExpand = it[PreferenceKeys.ALL_EXPAND] ?: false
            val sort = it[PreferenceKeys.SORT] ?: "NAME"
            val order = it[PreferenceKeys.ORDER] ?: "ASCENDING"
            SubCategoryPreferences(
                allExpand = allExpand,
                sort = enumValueOf(sort),
                sortOrder = enumValueOf(order)
            )
        }

    override suspend fun toggleAllExpand() {
        subCategoryDataStore.edit {
            val current = it[PreferenceKeys.ALL_EXPAND] ?: false
            it[PreferenceKeys.ALL_EXPAND] = !current
        }
    }

    override suspend fun changeSort(subCategorySort: SubCategorySort) {
        subCategoryDataStore.edit {
            it[PreferenceKeys.SORT] = subCategorySort.name
        }
    }

    override suspend fun changeOrder(sortOrder: SortOrder) {
        subCategoryDataStore.edit {
            it[PreferenceKeys.ORDER] = sortOrder.name
        }
    }
}

private object PreferenceKeys {
    val ALL_EXPAND = booleanPreferencesKey("all_expand")
    val SORT = stringPreferencesKey("sort")
    val ORDER = stringPreferencesKey("order")
}