package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkPreferenceRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    @AppScope appScope: CoroutineScope,
) : NetworkPreferenceRepository {
    private val dataStore = context.networkDatastore
    private val key = stringPreferencesKey("network")

    override val network = dataStore.data.catch {
        if (it is IOException) emit(emptyPreferences()) else throw it
    }.map { enumValueOf<NetworkPreferences>(it[key] ?: NetworkPreferences.ANY.name) }
        .shareIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override suspend fun networkSelected(network: NetworkPreferences) {
        dataStore.edit { it[key] = network.name }
    }
}

private const val NETWORK = "network preferences"
private val Context.networkDatastore by preferencesDataStore(name = NETWORK)

enum class NetworkPreferences { WIFI, ANY }