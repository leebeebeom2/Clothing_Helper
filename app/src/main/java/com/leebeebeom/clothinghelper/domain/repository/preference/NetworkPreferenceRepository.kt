package com.leebeebeom.clothinghelper.domain.repository.preference

import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferences
import kotlinx.coroutines.flow.Flow

interface NetworkPreferenceRepository {
    val network: Flow<NetworkPreferences>
    suspend fun networkSelected(network: NetworkPreferences)
}