package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

suspend inline fun <T> callbackFlowEmit(
    crossinline callback: () -> T?,
    flow: Flow<*>,
    crossinline emit: suspend (callback: T) -> Unit,
) {
    val callback1 = callback()

    if (callback1 != null) {
        emit(callback1)
        return
    }

    coroutineScope {
        val collectJob = launch { flow.collect() }

        while (collectJob.isActive) {
            val callback2 = callback() ?: continue

            emit(callback2)
            collectJob.cancel()
        }
    }
}