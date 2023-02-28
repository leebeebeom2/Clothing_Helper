package com.leebeebeom.clothinghelper.data.repository.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.FirebaseNetworkException
import com.leebeebeom.clothinghelper.data.repository.WifiException
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferences
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkChecker @Inject constructor(
    @ApplicationContext context: Context,
    private val networkPreferenceRepository: NetworkPreferenceRepository,
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCapabilities = connectivityManager.activeNetwork
    private val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)

    private suspend fun checkNetWork() {
        if (!networkCheck()) throw FirebaseNetworkException("인터넷 미연결")
        // 와이파이 선택 시 와이파이에 연결되지 않은 경우
        if (networkPreferenceRepository.network.first() == NetworkPreferences.WIFI && !isWifiConnected()) throw WifiException
    }

    fun isWifiConnected() =
        actNw?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

    fun networkCheck() = actNw?.let {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } ?: false
}


