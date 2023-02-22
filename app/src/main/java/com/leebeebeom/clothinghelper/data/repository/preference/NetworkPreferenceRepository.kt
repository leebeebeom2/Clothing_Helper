package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkPreferenceRepository @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.networkDatastore
    private val key = stringPreferencesKey("network")

    val network: Flow<NetworkPreferences> = dataStore.data.catch {
        if (it is IOException) emit(emptyPreferences()) else throw it
    }.map { enumValueOf(it[key] ?: NetworkPreferences.MOBILE.name) }

    suspend fun networkSelected(network: NetworkPreferences) {
        dataStore.edit { it[key] = network.name }
    }
}

private const val NETWORK = "network preferences"
private val Context.networkDatastore by preferencesDataStore(name = NETWORK)

enum class NetworkPreferences {
    LOCAL, WIFI, MOBILE
}