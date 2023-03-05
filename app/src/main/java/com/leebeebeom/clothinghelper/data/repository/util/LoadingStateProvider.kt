package com.leebeebeom.clothinghelper.data.repository.util

import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl.LoadingStateCallBack
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

        val callBack = LoadingStateCallBack { trySend(it) }

        this@LoadingStateProviderImpl.loadingStateCallBack = callBack
        awaitClose { this@LoadingStateProviderImpl.loadingStateCallBack = null }
    }.stateIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue
    )

    private var loadingStateCallBack: LoadingStateCallBack? = null

    suspend fun loadingOn() = callbackFlowEmitWrapper { it(true) }

    suspend fun loadingOff() = callbackFlowEmitWrapper { it(false) }
    private fun interface LoadingStateCallBack {
        operator fun invoke(loading: Boolean)
    }

    private suspend fun callbackFlowEmitWrapper(
        emit: suspend (LoadingStateCallBack) -> Unit,
    ) = callbackFlowEmit(
        { loadingStateCallBack },
        flow = isLoading,
        emit = emit
    )
}
