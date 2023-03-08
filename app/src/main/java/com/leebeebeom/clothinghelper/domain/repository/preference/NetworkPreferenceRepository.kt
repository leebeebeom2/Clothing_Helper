package com.leebeebeom.clothinghelper.domain.repository.preference

import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferences
import kotlinx.coroutines.flow.SharedFlow

interface NetworkPreferenceRepository {
    val network: SharedFlow<NetworkPreferences>
    suspend fun networkSelected(network: NetworkPreferences)
}