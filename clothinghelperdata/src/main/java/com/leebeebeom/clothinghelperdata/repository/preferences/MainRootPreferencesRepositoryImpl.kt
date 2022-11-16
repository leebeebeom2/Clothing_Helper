package com.leebeebeom.clothinghelperdata.repository.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelperdata.repository.util.AllExpandPreference
import com.leebeebeom.clothinghelperdomain.repository.MainScreenRootPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRootPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : AllExpandPreference(context.mainRootDatastore), MainScreenRootPreferencesRepository {
    override val allExpandKey = booleanPreferencesKey("all_expand")
}

private const val MAIN_ROOT = "main_root_preferences"
private val Context.mainRootDatastore by preferencesDataStore(name = MAIN_ROOT)