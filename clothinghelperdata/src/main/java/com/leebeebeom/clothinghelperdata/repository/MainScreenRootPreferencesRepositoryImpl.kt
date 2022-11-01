package com.leebeebeom.clothinghelperdata.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.leebeebeom.clothinghelperdomain.repository.MainScreenRootPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class MainScreenRootPreferencesRepositoryImpl(private val mainScreenRootDatastore: DataStore<Preferences>) :
    MainScreenRootPreferencesRepository {
    override val isAllExpand: Flow<Boolean>
        get() = mainScreenRootDatastore.data.catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }.map { it[MainScreenRootPreferenceKeys.ALL_EXPAND] ?: false }

    override suspend fun toggleAllExpand() {
        mainScreenRootDatastore.edit {
            val current = it[MainScreenRootPreferenceKeys.ALL_EXPAND] ?: false
            it[MainScreenRootPreferenceKeys.ALL_EXPAND] = !current
        }
    }
}

private object MainScreenRootPreferenceKeys {
    val ALL_EXPAND = booleanPreferencesKey("all_expand")
}