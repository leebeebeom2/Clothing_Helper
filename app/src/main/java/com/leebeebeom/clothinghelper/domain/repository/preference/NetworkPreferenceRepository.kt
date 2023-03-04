package com.leebeebeom.clothinghelper.domain.repository.preference

import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferences
import kotlinx.coroutines.flow.StateFlow

interface NetworkPreferenceRepository {
    val network: StateFlow<NetworkPreferences>
    suspend fun networkSelected(network: NetworkPreferences)
}