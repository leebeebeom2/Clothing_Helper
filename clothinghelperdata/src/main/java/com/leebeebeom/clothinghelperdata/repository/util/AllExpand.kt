package com.leebeebeom.clothinghelperdata.repository.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

abstract class AllExpandPreference(protected val dataStore: DataStore<Preferences>) {
    abstract val allExpandKey: Preferences.Key<Boolean>

    val isAllExpanded = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }.map { it[allExpandKey] ?: false }

    suspend fun toggleAllExpand() {
        dataStore.edit {
            val current = it[allExpandKey] ?: false
            it[allExpandKey] = !current
        }
    }
}