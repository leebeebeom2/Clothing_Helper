package com.leebeebeom.clothinghelper.data.repository.preference

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkPreferenceRepositoryTest {
    private lateinit var networkPreferenceRepository: NetworkPreferenceRepository
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        networkPreferenceRepository =
            RepositoryProvider(dispatcher).createNetworkPreferenceRepository()
    }

    @Test
    fun networkFlowTest() = runTest(dispatcher) {
        val networkFlow = networkPreferenceRepository.network

        backgroundScope.launch(dispatcher) { networkFlow.collectLatest { } }

        suspend fun assert(networkPreferences: NetworkPreferences) =
            assert(networkFlow.first() == networkPreferences)

        assert(networkPreferences = NetworkPreferences.ANY) // init value

        networkPreferenceRepository.networkSelected(NetworkPreferences.WIFI)
        assert(networkPreferences = NetworkPreferences.WIFI)

        networkPreferenceRepository.networkSelected(NetworkPreferences.ANY)
        assert(networkPreferences = NetworkPreferences.ANY)
    }
}