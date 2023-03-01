package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkPreferenceRepositoryImplTest {
    private lateinit var networkPreferenceRepository: NetworkPreferenceRepository

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        networkPreferenceRepository = NetworkPreferenceRepositoryImpl(context)
    }

    @Test
    fun networkFlowTest() = runTest {
        suspend fun assert(networkPreferences: NetworkPreferences) =
            assert(networkPreferenceRepository.network.first() == networkPreferences)

        assert(networkPreferences = NetworkPreferences.ANY) // 초기값

        networkPreferenceRepository.networkSelected(NetworkPreferences.WIFI)
        assert(networkPreferences = NetworkPreferences.WIFI)

        networkPreferenceRepository.networkSelected(NetworkPreferences.ANY)
        assert(networkPreferences = NetworkPreferences.ANY)
    }
}