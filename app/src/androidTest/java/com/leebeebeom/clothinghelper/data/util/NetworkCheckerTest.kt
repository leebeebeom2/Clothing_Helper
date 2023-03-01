package com.leebeebeom.clothinghelper.data.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferenceRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferences
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkCheckerTest {
    private lateinit var networkPreferenceRepository: NetworkPreferenceRepository
    private lateinit var networkChecker: NetworkChecker

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        networkPreferenceRepository = NetworkPreferenceRepositoryImpl(context)
        networkChecker = NetworkChecker(
            context = context,
            networkPreferenceRepository = networkPreferenceRepository
        )
    }

    @Test
    fun wifiCheckTest() = runTest {
        networkPreferenceRepository.networkSelected(network = NetworkPreferences.WIFI)
        networkChecker.checkNetWork()
        assert(true)
    }

    @Test
    fun connectivityTest() = runTest {
        networkPreferenceRepository.networkSelected(network = NetworkPreferences.ANY)
        networkChecker.checkNetWork()
        assert(true)
    }
}