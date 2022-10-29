package com.leebeebeom.clothinghelperdata.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.leebeebeom.clothinghelperdomain.repository.PreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    PreferencesRepository {

    override val subCategoryPreferences: Flow<SubCategoryPreferences> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map {
            val allExpand = it[PreferenceKeys.ALL_EXPAND] ?: false
            SubCategoryPreferences(allExpand)
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
}