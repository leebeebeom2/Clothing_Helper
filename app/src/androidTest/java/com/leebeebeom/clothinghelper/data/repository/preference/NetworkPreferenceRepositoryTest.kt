package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkPreferenceRepositoryTest {
    private lateinit var networkPreferenceRepository: NetworkPreferenceRepository

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        networkPreferenceRepository = NetworkPreferenceRepositoryImpl(
            context = context,
            appScope = RepositoryProvider.getAppScope()
        )
    }

    @Test
    fun networkFlowTest() = runTest {
        val networkFlow = networkPreferenceRepository.network

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            networkFlow.collectLatest { }
        }

        fun assert(networkPreferences: NetworkPreferences) =
            assert(networkFlow.value == networkPreferences)

        assert(networkPreferences = NetworkPreferences.ANY) // 초기값

        networkPreferenceRepository.networkSelected(NetworkPreferences.WIFI)
        assert(networkPreferences = NetworkPreferences.WIFI)

        networkPreferenceRepository.networkSelected(NetworkPreferences.ANY)
        assert(networkPreferences = NetworkPreferences.ANY)
    }
}