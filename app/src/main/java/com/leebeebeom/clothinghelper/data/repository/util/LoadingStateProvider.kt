package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

interface LoadingStateProvider {
    val isLoading: StateFlow<Boolean>
}

open class LoadingStateProviderImpl(
    initialValue: Boolean,
    appScope: CoroutineScope,
) : LoadingStateProvider {
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
    }.stateIn(appScope, SharingStarted.WhileSubscribed(5000), initialValue = initialValue)

    private var loadingStateCallBack: LoadingStateCallBack? = null

    fun loadingOn() = loadingStateCallBack?.loadingOn()

    fun loadingOff() = loadingStateCallBack?.loadingOff()
    private interface LoadingStateCallBack {
        fun loadingOn()
        fun loadingOff()
    }
}
