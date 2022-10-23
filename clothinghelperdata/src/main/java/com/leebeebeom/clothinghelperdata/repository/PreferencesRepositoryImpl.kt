package com.leebeebeom.clothinghelperdata.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.leebeebeom.clothinghelperdomain.repository.PreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    PreferencesRepository {

    override val preferencesFlow: Flow<UserPreferences> = dataStore.data.map {
        val allExpand = it[PreferenceKeys.ALL_EXPAND] ?: false
        UserPreferences(allExpand)
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