package com.leebeebeom.clothinghelper.data.util

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferences
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkCheckerTest {
    private lateinit var networkPreferenceRepository: NetworkPreferenceRepository
    private lateinit var networkChecker: NetworkChecker
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        networkPreferenceRepository = repositoryProvider.createNetworkPreferenceRepository()
        networkChecker =
            repositoryProvider.createNetWorkChecker(networkPreferenceRepository = networkPreferenceRepository)
    }
    // 에뮬레이터 수동 조작 해야됨
    @Test
    fun wifiCheckTest() = runTest(dispatcher) {
        networkPreferenceRepository.networkSelected(network = NetworkPreferences.WIFI)
        networkChecker.checkNetWork()
        assert(true)
    }

    @Test
    fun connectivityTest() = runTest(dispatcher) {
        networkPreferenceRepository.networkSelected(network = NetworkPreferences.ANY)
        networkChecker.checkNetWork()
        assert(true)
    }
}