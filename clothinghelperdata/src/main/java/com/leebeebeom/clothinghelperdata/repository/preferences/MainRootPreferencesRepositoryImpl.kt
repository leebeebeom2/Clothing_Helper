package com.leebeebeom.clothinghelperdata.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.leebeebeom.clothinghelperdata.repository.util.AllExpandPreference
import com.leebeebeom.clothinghelperdomain.repository.MainScreenRootPreferencesRepository

class MainRootPreferencesRepositoryImpl(dataStore: DataStore<Preferences>) :
    AllExpandPreference(dataStore), MainScreenRootPreferencesRepository {
    override val allExpandKey = booleanPreferencesKey("all_expand")
}