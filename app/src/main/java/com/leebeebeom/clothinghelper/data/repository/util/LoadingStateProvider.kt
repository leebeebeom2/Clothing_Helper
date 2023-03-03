package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface LoadingStateProvider {
    val isLoading: Flow<Boolean>
}

open class LoadingStateProviderImpl : LoadingStateProvider {
    override val isLoading = callbackFlow {

        val callBack = object : LoadingStateCallBack {
            override fun loadingOn() {
                trySend(true)
            }

            override fun loadingOff() {
                trySend(false)
            }
        }

        this@LoadingStateProviderImpl.loadingStateCallBack = callBack
        awaitClose { this@LoadingStateProviderImpl.loadingStateCallBack = null }
    }

    private var loadingStateCallBack: LoadingStateCallBack? = null

    fun loadingOn() = loadingStateCallBack?.loadingOn()

    fun loadingOff() = loadingStateCallBack?.loadingOff()
}

interface LoadingStateCallBack {
    fun loadingOn()
    fun loadingOff()
}