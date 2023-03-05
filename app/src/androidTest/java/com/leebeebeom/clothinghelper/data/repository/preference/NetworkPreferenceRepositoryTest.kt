package com.leebeebeom.clothinghelper.data.repository.preference

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkPreferenceRepositoryTest {
    private lateinit var networkPreferenceRepository: NetworkPreferenceRepository
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)

    @Before
    fun init() {
        networkPreferenceRepository = repositoryProvider.getNetworkPreferenceRepository()
    }

    @Test
    fun networkFlowTest() = runTest(dispatcher) {
        val networkFlow = networkPreferenceRepository.network

        backgroundScope.launch(dispatcher) { networkFlow.collectLatest { } }

        fun assert(networkPreferences: NetworkPreferences) =
            assert(networkFlow.value == networkPreferences)

        assert(networkPreferences = NetworkPreferences.ANY) // 초기값

        networkPreferenceRepository.networkSelected(NetworkPreferences.WIFI)
        assert(networkPreferences = NetworkPreferences.WIFI)

        networkPreferenceRepository.networkSelected(NetworkPreferences.ANY)
        assert(networkPreferences = NetworkPreferences.ANY)
    }
}