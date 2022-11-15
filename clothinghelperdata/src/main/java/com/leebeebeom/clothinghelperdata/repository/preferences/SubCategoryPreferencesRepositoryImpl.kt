package com.leebeebeom.clothinghelperdata.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.leebeebeom.clothinghelperdata.repository.util.AllExpandPreference
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SubCategoryPreferencesRepositoryImpl(dataStore: DataStore<Preferences>) :
    AllExpandPreference(dataStore), SubCategoryPreferencesRepository {

    override val sort: Flow<SubCategorySortPreferences> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map {
            val sort = it[SubCategoryPreferenceKeys.SORT] ?: SubCategorySort.NAME.name
            val order = it[SubCategoryPreferenceKeys.ORDER] ?: SortOrder.ASCENDING.name
            SubCategorySortPreferences(enumValueOf(sort), enumValueOf(order))
        }

    override suspend fun changeSort(subCategorySort: SubCategorySort) {
        dataStore.edit {
            it[SubCategoryPreferenceKeys.SORT] = subCategorySort.name
        }
    }

    override suspend fun changeOrder(sortOrder: SortOrder) {
        dataStore.edit {
            it[SubCategoryPreferenceKeys.ORDER] = sortOrder.name
        }
    }

    override val allExpandKey = SubCategoryPreferenceKeys.ALL_EXPAND
}

private object SubCategoryPreferenceKeys {
    val ALL_EXPAND = booleanPreferencesKey("all_expand")
    val SORT = stringPreferencesKey("sort")
    val ORDER = stringPreferencesKey("order")
}