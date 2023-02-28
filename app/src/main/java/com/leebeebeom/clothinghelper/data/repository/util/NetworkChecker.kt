package com.leebeebeom.clothinghelper.data.repository.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkChecker @Inject constructor(@ApplicationContext context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCapabilities = connectivityManager.activeNetwork
    private val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)

    fun isWifiConnected() =
        actNw?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

    fun networkCheck() = actNw?.let {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } ?: false
}


