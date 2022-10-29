package com.leebeebeom.clothinghelperdata.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SubCategoryPreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    SubCategoryPreferencesRepository {

    override val subCategoryPreferences: Flow<SubCategoryPreferences> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map {
            val allExpand = it[PreferenceKeys.ALL_EXPAND] ?: false
            val sort = it[PreferenceKeys.SORT] ?: "name"
            val order = it[PreferenceKeys.ORDER] ?: "ascending"
            SubCategoryPreferences(
                allExpand = allExpand,
                sort = enumValueOf(sort),
                sortOrder = enumValueOf(order)
            )
        }

    override suspend fun toggleAllExpand() {
        dataStore.edit {
            val current = it[PreferenceKeys.ALL_EXPAND] ?: false
            it[PreferenceKeys.ALL_EXPAND] = !current
        }
    }
}

private object PreferenceKeys {
    val ALL_EXPAND = booleanPreferencesKey("all_expand")
    val SORT = stringPreferencesKey("sort")
    val ORDER = stringPreferencesKey("order")
}